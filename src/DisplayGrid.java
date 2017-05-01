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
    private int GridToScreenRatio;
    private GridObject[][] world;

    DisplayGrid(GridObject[][] w) {
        this.world = w;

        int maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
        int maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
        GridToScreenRatio = maxY / (world.length+1);  //ratio to fit in screen as square map

        System.out.println("Map size: " + world.length + " by "+world[0].length + "\nScreen size: " + maxX + "x" + maxY + " Ratio: " + GridToScreenRatio);

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

            for(int col = 0; col<world[0].length;col=col+1) {
                for(int row = 0; row<world.length;row=row+1) {

                    //Grass background tile
                    g.drawImage(grass, row * GridToScreenRatio, col * GridToScreenRatio, GridToScreenRatio, GridToScreenRatio, this);

                    //Declare variables
                    GridObject object = world[col][row];
                    int xVal = row * GridToScreenRatio;
                    int yVal = col * GridToScreenRatio;
                    int width = GridToScreenRatio;
                    int height = GridToScreenRatio;

                    Image objectImage = null;
                    Image genderIcon = null;

                    if (object instanceof Sheep) {
                        objectImage = sheep;
                        genderIcon = object.getGender() ? maleIcon : femaleIcon;
                    } else if (object instanceof Wolf) {
                        objectImage = wolf;
                        genderIcon = object.getGender() ? maleIcon : femaleIcon;
                    } else if (object instanceof Plant) {
                        objectImage = plant;
                    }

                    if (objectImage != null) {
                        g.drawImage(objectImage, xVal, yVal, width, height, this);
                    }

                    if (genderIcon != null) {
                        g.drawImage(genderIcon, xVal, yVal, width/5, height/5, this);
                    }


                }
            }
        }
    }//end of GridAreaPanel

} //end of DisplayGrid
