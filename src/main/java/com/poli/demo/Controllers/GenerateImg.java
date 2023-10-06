package com.poli.demo.Controllers;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@RequestMapping("api/")
@RestController
public class GenerateImg {

    @GetMapping("/getImg")
    public String getImg() throws IOException
    {
        String[] stations = {"Portal Norte","Toberín","Polo","Cad","Cardio Infantil","Mazurén","Calle 146","Calle 142","Alcalá","Prado"};

        //se calcula el tamaño de los nombre de las estaciones
        int stationsW = 0;
        for (int i = 0; i < stations.length; i++) {
            stationsW += (stations[i].length()*3)+15;
        }
        //se define el tamaño de la imagen
        //int width = stationsW;
        int height = 100;

        BufferedImage bufferedImage = new BufferedImage(stationsW, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        // se rellena la imagen con color blanco
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, stationsW, height);

        //se ponen las cordenadas iniciales de los rectangulos que representan las estaciones
        int[] xInit = {5+(stations[0].length()*3),10+(stations[0].length()*3),10+(stations[0].length()*3),5+(stations[0].length()*3)};
        int[] yInit = {25,25,35,35};

        g2d.setColor(Color.gray);

        for (int i = 0; i < stations.length; i++) {
            int[] xPoints = xInit;

            Polygon rectangle = new Polygon(xPoints,yInit,4);
            g2d.drawPolygon(rectangle);
            g2d.fillPolygon(rectangle);
            if(i % 2 == 0)
            {
                g2d.setColor(Color.black);
                int xPosStation = xPoints[0]- (stations[i].length()*3);
                g2d.drawString(stations[i],xPosStation+5,yInit[0]-10);
                g2d.setColor(Color.gray);
                for (int j = 0; j < 4; j++) {
                    yInit[j] += 20;
                }
            } else
            {
                g2d.setColor(Color.black);
                int xPosStation = xPoints[0]- (stations[i].length()*3);
                g2d.drawString(stations[i],xPosStation+5,yInit[0]+20);
                g2d.setColor(Color.gray);
                for (int j = 0; j < 4; j++) {
                    yInit[j] -= 20;
                }
            }

            if (i != stations.length-1)
            {
                for (int j = 0; j < 4; j++) {
                    xPoints[j] += (stations[i+1].length() * 3)+10;
                }
            }

            xInit = xPoints;

        }

        //se asigna el color para pintar el rectangulo central que representa la ruta
        g2d.setColor(Color.black);
        //int lastXPos = xInit[1]-(stations[(stations.length-1)].length()*3);
        int lastXPos = xInit[1];
        //se crea el rectangulo central que representa las estaciones
        int[] xLine = {(stations[0].length()*3)+5,lastXPos,lastXPos,(stations[0].length()*3)+5};
        int[] yLine = {36,36,44,44};
        Polygon generaLine = new Polygon(xLine,yLine,4);
        g2d.drawPolygon(generaLine);
        g2d.fillPolygon(generaLine);

        // Disposes of this graphics context and releases any system resources that it is using.
        g2d.dispose();

        // Save as PNG
        File file = new File("myimage.png");
        ImageIO.write(bufferedImage, "png", file);

        //se para la imgen a base 64
        byte[] fileContent = FileUtils.readFileToByteArray(file);
        String encodedString = Base64.getEncoder().encodeToString(fileContent);

        return encodedString;
    }
}
