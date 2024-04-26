package lemmings.level;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Terrain {

    Area terrain;
    Area leftStairs;
    Area rightStairs;
    Area invisibleTerrain;
    Area unmineableTerrain;
    boolean[][] isUnmineable;
    private static BufferedImage noiseTexture;
    private static double[][] lavaTexturePoints;

    static {
        try {
            noiseTexture = ImageIO.read(new File("assets/noiseTexture.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        lavaTexturePoints = new double[25 * 25][2];
        for (int i = 0; i < lavaTexturePoints.length;i++){
            lavaTexturePoints[i] = new double[]{noiseTexture.getWidth() * Math.random(), noiseTexture.getHeight() * Math.random()};
        }
    }

    public Terrain(int width, int height){
        isUnmineable = new boolean[width][height];
        terrain = new Area();
        leftStairs = new Area();
        rightStairs = new Area();
        invisibleTerrain = new Area();
        unmineableTerrain = new Area();
    }

    public void add(Shape s){
        terrain.add(new Area(s));
    }

    public void addStair(Shape s,boolean right){
        (right? rightStairs:leftStairs).add(new Area(s));
    }

    public void subtract(Shape s){
        terrain.subtract(new Area(s));
    }

    public boolean intersectsUnmineable(Rectangle2D s){
        return unmineableTerrain.intersects(s) || unmineableTerrain.contains(s);
    }

    public boolean intersects(Rectangle2D s, boolean includeRightStairs, boolean includeLeftStairs, boolean includeUnmineable, boolean includeInvisible){
        boolean result = terrain.intersects(s) || terrain.contains(s);
        if (includeRightStairs){
            result = result || rightStairs.intersects(s) || rightStairs.contains(s);
        } if (includeLeftStairs){
            result = result || leftStairs.intersects(s) || leftStairs.contains(s);
        } if (includeUnmineable){
            result = result || intersectsUnmineable(s);
        } if (includeInvisible) {
            result = result || invisibleTerrain.intersects(s) || invisibleTerrain.contains(s);
        }
        return result;
    }

    public BufferedImage getTerrainTexture(int levelNum, int width, int height, Theme theme){
        Random random = new Random(levelNum);
        int x = (int)((1000 - width) * random.nextDouble());
        int y = (int)((1000 - height) * random.nextDouble());
        BufferedImage subImage = clone(noiseTexture.getSubimage(x,y,width,height));
        Area terrainInverse = new Area(new Rectangle(0,0,width,height));
        terrainInverse.subtract(terrain);
        terrainInverse.subtract(unmineableTerrain);
        Graphics2D g2 = (Graphics2D) subImage.getGraphics();
        g2.setColor(Color.RED);
        g2.fill(terrainInverse);
        BufferedImage output = new BufferedImage(width,height,BufferedImage.TRANSLUCENT);
        if (theme == Theme.DEFAULT){
            colorDefaultTheme(output,subImage,random,width,height);
        } else if (theme == Theme.LAVA) {
            colorLavaTheme(output, subImage, x, y, width, height);
        }
        g2 = output.createGraphics();
        g2.setColor(new Color(120,77,0));
        g2.fill(rightStairs);
        g2.fill(leftStairs);
        return output;
    }

    private void colorLavaTheme(BufferedImage output, BufferedImage subImage,int x, int y, int width, int height){
        for (int i = 0; i < width;i++){
            for (int j = 0; j < height;j++){
                int pixelColor = subImage.getRGB(i,j);
                if (pixelColor != Color.RED.getRGB()){
                    float intensity = (int)(new Color(pixelColor).getRed() / 32f) / 8f;
                    double minDistance = Double.MAX_VALUE;
                    double minDistance2 = Double.MAX_VALUE;
                    for (int k = 0; k < lavaTexturePoints.length;k++){
                        double distance = (x + i - lavaTexturePoints[k][0])*(x + i - lavaTexturePoints[k][0]);
                        distance += (y + j - lavaTexturePoints[k][1])*(y + j - lavaTexturePoints[k][1]);
                        if (distance < minDistance){
                            minDistance = distance;
                        } if (distance < minDistance2 && minDistance != distance){
                            minDistance2 = distance;
                        }
                    }
                    int newColor;
                    if (!isUnmineable[i][j]) {
                        if (Math.abs(minDistance - minDistance2) > 36) {
                            newColor = Color.HSBtoRGB(0f / 360, 0.f, 0.2f * intensity);
                        } else {
                            newColor = Color.HSBtoRGB((50f - 40f * intensity) / 360f, 1.0f, 1f);
                        }
                    } else {
                        newColor = Color.HSBtoRGB((50f - 40f * intensity) / 360f, 1.0f, 1f);
                    }
                    output.setRGB(i,j,newColor);
                }
            }
        }
    }

    private void colorDefaultTheme(BufferedImage output, BufferedImage subImage, Random random, int width, int height){
        double[] doubles = random.doubles(width * height).toArray();
        for (int i = 0; i < width;i++){
            for (int j = 0; j < height;j++){
                int pixelColor = subImage.getRGB(i,j);
                if (pixelColor != Color.RED.getRGB()){
                    float intensity = (int)(new Color(pixelColor).getRed() / 32f) / 8f;
                    int distance = 0;
                    int color = pixelColor;
                    while (j - distance >= 0 && color != Color.RED.getRGB()){
                        color = subImage.getRGB(i,j - distance);
                        distance++;
                        if (j - distance < 0){
                            distance = 1000;
                        }
                    }
                    distance--;
                    int newColor;
                    if (!isUnmineable[i][j]) {
                        if (distance <= 7 && doubles[i * height + j] <= 1.0 / Math.pow(distance, 1.1)) {
                            newColor = Color.HSBtoRGB(120.0f / 360, 0.8f, 1f - 0.8f * intensity);
                        } else {
                            newColor = Color.HSBtoRGB(29.0f / 360, 0.87f, 0.2f + 0.8f * intensity);
                        }
                    } else {
                        newColor = Color.HSBtoRGB(0f,0f,0.7f - 0.4f * intensity);
                    }
                    output.setRGB(i,j,newColor);
                }
            }
        }
    }

    private BufferedImage clone(BufferedImage bufferedImage){
        BufferedImage output = new BufferedImage(bufferedImage.getWidth(),bufferedImage.getHeight(),BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < bufferedImage.getWidth();i++){
            for (int j = 0; j < bufferedImage.getHeight();j++){
                output.setRGB(i,j,bufferedImage.getRGB(i,j));
            }
        }
        return output;
    }

    public void addInvisible(Shape shape) {
        invisibleTerrain.add(new Area(shape));
    }

    public void subtractInvisible(Shape shape){
        invisibleTerrain.subtract(new Area(shape));
    }

    public void addUnmineable(Rectangle rectangle) {
        unmineableTerrain.add(new Area(rectangle));
        if (rectangle.x >= 0 && rectangle.x < isUnmineable.length && rectangle.y >= 0 && rectangle.y < isUnmineable[0].length) {
            isUnmineable[rectangle.x][rectangle.y] = true;
        }
    }

    public boolean intersectsInvisible(Rectangle2D right) {
        return invisibleTerrain.intersects(right) || invisibleTerrain.contains(right);
    }
}
