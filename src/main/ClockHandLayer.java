/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JLabel;

/**
 *
 * @author Thusitha
 */
public class ClockHandLayer extends JLabel{

    public static int count;
    private Color mainColor;
    int thickness;
    /**
     * Creates new form clock
     */
    final Point[][] base_pt = new Point[3][2];

    Timer t;
    Date cur, prev;
    Boolean Change;
    
    public ClockHandLayer() {
        super();
        base_pt[0][0] = new Point(0, 0);
        base_pt[0][1] = new Point(0, -150);
        base_pt[1][0] = new Point(0, 0);
        base_pt[1][1] = new Point(0, -170);
        base_pt[2][0] = new Point(0, 0);
        base_pt[2][1] = new Point(0, -170);
        t = new Timer();
        t.schedule(new ClockHandLayer.SecondsTask(), 1000, 1000);
        mainColor=Color.ORANGE;
        thickness=10;
    }
    
    
    private void setLengths(){
        int length = getWidth()>getHeight()?getHeight()/2:getWidth()/2;
        base_pt[0][1] = new Point(0, -(length-(length*50/100)));
        base_pt[1][1] = new Point(0, -(length-(length*40/100)));
        base_pt[2][1] = new Point(0, -(length-(length*30/100)));
        
        thickness=length*3/100;
    }
    

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
        if (prev != null) {
            g2d.setColor(mainColor);
            DrawHands(g2d, prev, true);
        }

    }

    private void DrawHands(Graphics g, Date dt, Boolean ichange) {
        Point[][] pt_temp = new Point[3][2];
        int[] iAngle = new int[3];
        iAngle[0] = (int) ((dt.getHours() * 30) % 360 + dt.getMinutes() / 2);
        iAngle[1] = (int) (dt.getMinutes() * 6);
        iAngle[2] = (int) (dt.getSeconds() * 6);
        setLengths();
        pt_temp = base_pt.clone();
        for (int i = ichange ? 0 : 2; i < 3; i++) {
            int pres = (thickness/(i+1));
            Point pt[] = new Point[1];
            pt[0] = pt_temp[i][1];
            this.RotatePoint(pt, 1, iAngle[i]);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(pres));
            g2.draw(new Line2D.Float(0, 0, pt[0].x, pt[0].y));
        }
    }

    private void RotatePoint(Point pt[], int iRotate, int iAngle) {
        Point ptTemp = new Point();
        for (int i = 0; i < iRotate; i++) {
            ptTemp.x = (int) (pt[i].x * Math.cos(2 * Math.PI * iAngle / 360) - pt[i].y * Math.sin(2 * Math.PI * iAngle / 360));
            ptTemp.y = (int) (pt[i].y * Math.cos(2 * Math.PI * iAngle / 360) - pt[i].x * Math.sin(2 * Math.PI * iAngle / 360));
            pt[i] = ptTemp;

        }
    }

    public Color getMainColor() {
        return mainColor;
    }

    public void setMainColor(Color mainColor) {
        this.mainColor = mainColor;
    }

    class SecondsTask extends TimerTask {

        @Override
        public void run() {
            cur = new Date();
            if (prev != null) {
                Change = cur.getHours() != prev.getHours() || cur.getMinutes() != prev.getMinutes();
                //ClockHandLayer.this.getGraphics().setColor(Color.red);
                //ClockHandLayer.this.getGraphics().setColor(Color.black);
                DrawHands(ClockHandLayer.this.getGraphics(), cur, Change);
                DrawHands(ClockHandLayer.this.getGraphics(), cur, true);
            }
            prev = cur;
            ClockHandLayer.this.repaint();
        }

    }
}
