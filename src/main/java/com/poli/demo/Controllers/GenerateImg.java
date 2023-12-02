package com.poli.demo.Controllers;

import com.poli.demo.Models.In.Route;
import com.poli.demo.Models.In.Station;
import org.apache.commons.io.FileUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

@RequestMapping("api/")
@RestController
public class GenerateImg {

    @PostMapping("/getImg")
    public String getImg(@RequestBody Route route) throws IOException
    {
        try
        {

          Station[] stations =route.getStations();

          //se calcula el tamaño de los nombre de las estaciones
          int stationsW = this.getW(stations);

          //se define el tamaño de la imagen
          int preHeight = stations.length/10;
          int height = ((preHeight + 1) * 95 ) + 30;

          BufferedImage bufferedImage = new BufferedImage(stationsW, height, BufferedImage.TYPE_INT_RGB);

          Graphics2D g2d = bufferedImage.createGraphics();

          // se rellena la imagen con color blanco
          g2d.setColor(Color.white);
          g2d.fillRect(0, 0, stationsW, height);

          g2d.setFont(new Font("cowrier new",1,11));
          g2d.setColor(Color.black);
          String duration = "Tiempo estimado: "+route.getDuration()+" min";

          g2d.drawString(duration,stationsW/6,17);

          //se ponen las cordenadas iniciales de los rectangulos que representan las estaciones
          int[] xInit = {5+(stations[0].getName().length()*3),
                  10+(stations[0].getName().length()*3),
                  10+(stations[0].getName().length()*3),
                  5+(stations[0].getName().length()*3)};
          int[] yInit = {50,50,60,60};

          g2d.setColor(Color.gray);

          for (int i = 0; i < stations.length; i++) {
              Color stationColor = Color.green;
              //if(!stations[i].getStand()) stationColor = Color.red;
              g2d.setColor(stationColor);

              int[] xPoints = xInit;

              Polygon rectangle = new Polygon(xPoints,yInit,4);
              g2d.drawPolygon(rectangle);
              g2d.fillPolygon(rectangle);
              g2d.setColor(Color.black);
              if(i % 2 == 0)
              {
                  int xPosStation = xPoints[1] - (stations[i].getName().length() * 3);
                  g2d.drawString(stations[i].getName(),xPosStation,yInit[0]);
                  for (int j = 0; j < 4; j++) {
                      yInit[j] += 20;
                  }
              } else
              {
                  int xPosStation = xPoints[1]- (stations[i].getName().length() * 3);
                  g2d.drawString(stations[i].getName(),xPosStation,yInit[0]+20);
                  for (int j = 0; j < 4; j++) {
                      yInit[j] -= 20;
                  }
              }

              int mod = 10;
              if ((i == 0 || (i > 1 && i % mod == 0)) && i < stations.length-1)
              {
                  for (int j = 0; j < 4; j++)
                  {
                      xPoints[j] += (stations[i+1].getName().length() * 2);
                  }
              }   else if(i < stations.length-1)
              {
                  for (int j = 0; j < 4; j++)
                  {
                      xPoints[j] += (stations[i-1].getName().length() * 3);
                      xPoints[j] += (stations[i+1].getName().length() * 3);
                  }
              }

              xInit = xPoints;

              if(i > 1 && i < stations.length-1 && i % mod == 0)
              {
                  int lastXPos = xInit[1];
                  xInit[0] = 5+(stations[i+1].getName().length()*3);
                  xInit[1] = 10+(stations[i+1].getName().length()*3);
                  xInit[2] = 10+(stations[i+1].getName().length()*3);
                  xInit[3] = 5+(stations[i+1].getName().length()*3);
                  g2d.setColor(Color.blue);
                  int[] xLine = {5+(stations[i+1-mod].getName().length()*3),
                          lastXPos,
                          lastXPos,
                          5+(stations[i+1-mod].getName().length()*3)};
                  if(i == 10)
                  {
                      xLine[0] = 5+(stations[0].getName().length()*3);
                      xLine[3] = 5+(stations[0].getName().length()*3);
                  }
                  int[] yLine = {yInit[0]-10,yInit[1]-10,yInit[2]-10,yInit[3]-10};
                  Polygon generaLine = new Polygon(xLine,yLine,4);
                  g2d.drawPolygon(generaLine);
                  g2d.fillPolygon(generaLine);
                  yInit[0] = yInit[0]+90;
                  yInit[1] = yInit[1]+90;
                  yInit[2] = yInit[2]+90;
                  yInit[3] = yInit[3]+90;
              }
          }

          //se pinta la ultima linea azul
          g2d.setColor(Color.blue);
          int lastXPos = xInit[1];

          int diss = (stations.length % 10 == 0) ? 10 : stations.length % 10;
          int[] xLine = {5+(stations[stations.length + 1 - diss].getName().length()*3),
                  lastXPos,
                  lastXPos,
                  5+(stations[stations.length + 1 - diss].getName().length()*3)};
          int desfase = stations.length % 2 == 0 ? 10 : -10;
          int[] yLine = {yInit[0]+desfase,yInit[1]+desfase,yInit[2]+desfase,yInit[3]+desfase};
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
        } catch (Exception e)
        {
            return e.toString()+' '+e.getLocalizedMessage();
        }
    }


    private int getW(Station[] stations )
    {
        int stationsW = 10;
        int maxW = 10;
        for (int i = 0; i < stations.length; i++)
        {
            stationsW += ((stations[i].getName().length()*5.5)+22);
            if(i % 10 == 0 || i  == stations.length-1)
            {
                maxW = stationsW > maxW ? stationsW : maxW;
                stationsW = 10;
            }
        }
        return maxW;
    }
}
