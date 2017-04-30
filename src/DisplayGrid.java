/* [DisplayGrid.java]
 * A Small program for Display a 2D String Array graphically
 * @author Mangat
 */

// Graphics Imports
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;


class DisplayGrid {

    private JFrame frame;
    private int maxX,maxY, GridToScreenRatio;
    private GridObject[][] world;

    DisplayGrid(GridObject[][] w) {
        this.world = w;

        maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
        maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
        GridToScreenRatio = maxY / (world.length+1);  //ratio to fit in screen as square map

        System.out.println("Map size: "+world.length+" by "+world[0].length + "\nScreen size: "+ maxX +"x"+maxY+ " Ratio: " + GridToScreenRatio);

        this.frame = new JFrame("Map of World");

        GridAreaPanel worldPanel = new GridAreaPanel();

        frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }


    public void refresh() {
        frame.repaint();
    }



    class GridAreaPanel extends JPanel {
        public void paintComponent(Graphics g) {

            //Import images
            Image sheep = Toolkit.getDefaultToolkit().getImage("res/sheep.png");
            Image wolf = Toolkit.getDefaultToolkit().getImage("res/wolf.png");
            Image plant = Toolkit.getDefaultToolkit().getImage("res/plant.png");
            Image grass = Toolkit.getDefaultToolkit().getImage("res/grass.png");
            Image maleIcon = Toolkit.getDefaultToolkit().getImage("res/male.png");
            Image femaleIcon = Toolkit.getDefaultToolkit().getImage("res/female.png");

            setDoubleBuffered(true);
            g.setColor(Color.BLACK);

            for(int i = 0; i<world[0].length;i=i+1)
            {
                for(int j = 0; j<world.length;j=j+1) {

                    //Grass background tile
                    g.drawImage(grass, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);

                    if (world[i][j] instanceof Sheep) {
                        g.drawImage(sheep, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);
                        if (world[i][j].getGender()) {
                            g.drawImage(maleIcon, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio / 5, GridToScreenRatio / 5, this);
                        } else {
                            g.drawImage(femaleIcon, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio / 5, GridToScreenRatio / 5, this);
                        }
                    } else if (world[i][j] instanceof Wolf) {
                        g.drawImage(wolf, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);
                        if (world[i][j].getGender()) {
                            g.drawImage(maleIcon, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio / 5, GridToScreenRatio / 5, this);
                        } else {
                            g.drawImage(femaleIcon, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio / 5, GridToScreenRatio / 5, this);
                        }
                    } else if (world[i][j] instanceof Plant) {
                        g.drawImage(plant, j * GridToScreenRatio, i * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);
                    }

                }
            }
        }
    }//end of GridAreaPanel

} //end of DisplayGrid
