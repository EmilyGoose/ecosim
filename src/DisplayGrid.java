/* [DisplayGrid.java]
 * A Small program for Display a 2D String Array graphically
 * @author Mangat
 */

// Graphics Imports

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.io.FileInputStream;
import java.net.URL;

//IntelliJ-specific line to stop annoying "access can be package-private" warnings
@SuppressWarnings("WeakerAccess")

class DisplayGrid {

    private JFrame frame;
    private int GridToScreenRatio;
    private GridObject[][] world;

    private static Image sheep;
    private static Image wolf;
    private static Image plant;
    private static Image deadPlant;
    private static Image yellowPlant;
    private static Image grass;
    private static Image maleIcon;
    private static Image femaleIcon;

    DisplayGrid(GridObject[][] w) {

        //Import images
        sheep = Toolkit.getDefaultToolkit().getImage("res/sheep.png");
        wolf = Toolkit.getDefaultToolkit().getImage("res/wolf.png");
        plant = Toolkit.getDefaultToolkit().getImage("res/plant.png");
        deadPlant = Toolkit.getDefaultToolkit().getImage("res/plant_dead.png");
        yellowPlant = Toolkit.getDefaultToolkit().getImage("res/plant_yellow.png");
        grass = Toolkit.getDefaultToolkit().getImage("res/grass.png");
        maleIcon = Toolkit.getDefaultToolkit().getImage("res/male.png");
        femaleIcon = Toolkit.getDefaultToolkit().getImage("res/female.png");

        this.world = w;

        int maxX = Toolkit.getDefaultToolkit().getScreenSize().width;
        int maxY = Toolkit.getDefaultToolkit().getScreenSize().height;
        GridToScreenRatio = maxY / (world.length+1);  //ratio to fit in screen as square map

        System.out.println("Map size: " + world.length + " by "+world[0].length + "\nScreen size: " + maxX + "x" + maxY + " Ratio: " + GridToScreenRatio);

        this.frame = new JFrame("Misha's ECO Sim");

        GridAreaPanel worldPanel = new GridAreaPanel();

        frame.getContentPane().add(BorderLayout.CENTER, worldPanel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        frame.setVisible(true);
    }


    public void refresh() {
        frame.repaint();
    }



    class GridAreaPanel extends JPanel {
        public void paintComponent(Graphics g) {

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
                        objectImage = object.getHealth() > 0 ? (object.getHealth() > 5 ? plant : yellowPlant) : deadPlant;
                    }

                    if (objectImage != null) {
                        g.drawImage(objectImage, xVal, yVal, width, height, this);
                    }

                    if (genderIcon != null) {
                        g.drawImage(genderIcon, xVal, yVal, width / 5, height / 5, this);
                    }
                }
            }

            //Count returns [plants, sheep, wolves]
            int[] count = EcoSim.countObjects();

            g.setFont(new Font("Segoe UI Light", Font.PLAIN, 20));

            int fontX = world[0].length * GridToScreenRatio + 20;

            //Draw numbers for sheep, wolves, etc...
            g.drawString("Plants : " + count[0], fontX, 20);
            g.drawString("Sheep : " + count[1], fontX, 40);
            g.drawString("Wolves : " + count[2], fontX, 60);
        }
    }//end of GridAreaPanel

} //end of DisplayGrid
