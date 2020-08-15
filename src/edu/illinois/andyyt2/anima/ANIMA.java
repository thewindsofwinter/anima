package edu.illinois.andyyt2.anima;

// Import necessary packages
import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 * ANIMA (Analysis of Neural Images through Machine Algorithms)
 * is a Java-based image processing software specialized for
 * immunohistochemically stained neuron images.
 *
 * @author Andy Tang
 */
public class ANIMA extends javax.swing.JFrame {
    // Variables describing the guide stain image and image to be analyzed
    private BufferedImage guideImage;
    private BufferedImage analysisImage;

    // Variables which will store thresholds to measure the "edges" of each cell in the image
    private int thresh;
    private int gThresh;

    // Two-dimensional array for the breadth-first search
    private int[][] visited;
    
    // Variables keeping track of which cell is currently being analyzed
    private int currentGrp;
    private int count;
    private ArrayDeque<Integer> values;
    private String path;

    // A file chooser in the GUI (Graphical User Interface)
    private final JFileChooser fc = new JFileChooser();

    // Constants to help determine edge of cell in guide and analysis essay
    private final double GUIDE_DEVIATION = 1.0;
    private final double ANALYSIS_DEVIATION = 4.0;
    private final double MIN_ASTROCYTE_SIZE = 100.0;
    private final double MIN_NUCLEUS_SIZE = 500.0;

    // Properties of the computer that is measured on initialization
    private String username;
    private final String FILE_SEPERATOR = File.separator;

    /**
     * Creates new form ANIMA
     */
    public ANIMA() {
        // Initialize variables
        initComponents();
        guideImage = null;
        analysisImage = null;
        thresh = -1;
        gThresh = -1;
        values = new ArrayDeque<>();
        path = "";

        // Measure the user home folder
        username = System.getProperty("user.home");

        // Initialize Breadth-First Search algorithm
        visited = new int[0][0];
        currentGrp = 1;

        // Read in old data
        BufferedReader f;
        
        // Create directories for analysis engine if they do not already exist
        try {
            boolean directory = new File(username + FILE_SEPERATOR + "Documents/AnalysisEngine").mkdirs();
            if(!directory) {
                boolean count = new File(username + FILE_SEPERATOR + "Documents/AnalysisEngine"
                        + FILE_SEPERATOR + "count").mkdirs();
                boolean maps = new File(username + FILE_SEPERATOR + "Documents/AnalysisEngine"
                        + FILE_SEPERATOR + "maps").mkdirs();
                boolean data = new File(username + FILE_SEPERATOR + "Documents/AnalysisEngine"
                        + FILE_SEPERATOR + "data").mkdirs();

            }

            // Create run count file if it does not already exist
            boolean exists = new File(username + FILE_SEPERATOR + "AnalysisEngine"
                    + FILE_SEPERATOR + "count" + FILE_SEPERATOR + "count.txt").isFile();
            if(!exists) {
                PrintWriter out = new PrintWriter(new FileWriter(username + FILE_SEPERATOR +
                        "Documents/AnalysisEngine" + FILE_SEPERATOR + "count" + FILE_SEPERATOR + "count.txt"));
                out.println(0);
                out.close();
            }

            // Read in run number
            f = new BufferedReader(new FileReader(username + "/Documents/AnalysisEngine/count/count.txt"));
            count = Integer.parseInt(f.readLine());
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ANIMA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ANIMA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Log current run number
        jTextArea1.setText("Image Analyzer - Run #" + count);
    }
    
    // Method to get the blue value in the integer representation of a color
    private int getBlue(int a) {
        Color c = new Color(a);
        return c.getBlue();
    }
    
    // Method to scan the images using thresholds for astrocyte and nucleus detection
    private void scanImages() {
        jTextArea1.setText(jTextArea1.getText() + "\n" +"Scanning guide (may take a while)...");
        
        // Run a breadth-first search on the graph
        visited = new int[guideImage.getWidth()][guideImage.getHeight()];
        currentGrp = 1;
        Queue<Integer> q = new ArrayDeque<>();
        
        for(int i = 0; i < guideImage.getWidth(); i++) {
            for(int j = 0; j < guideImage.getHeight(); j++) {
                // Start at any unvisited point which is both a part of an astrocyte and a nucleus
                if(getBlue(guideImage.getRGB(i, j)) > gThresh 
                        && getBlue(analysisImage.getRGB(i, j)) > thresh 
                        && visited[i][j] == 0) {
                    visited[i][j] = currentGrp;
                    
                    // Keep track of how many cells are over the threshold
                    int ct = 1;
                    q.add(i*guideImage.getHeight() + j);
                    
                    while(!q.isEmpty()) {
                        int hash = q.poll();
                        int tempI = hash / guideImage.getHeight();
                        int tempJ = hash % guideImage.getHeight();
                        
                        // Permute over all directions not yet visited
                        int[][] permute = {{-1, -1}, {-1, 0}, {-1, 1}, 
                        {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
                        
                        for (int[] prm : permute) {
                            if (tempI + prm[0] >= 0 
                                    && tempI + prm[0] < guideImage.getWidth() 
                                    && tempJ + prm[1] >= 0 
                                    && tempJ + prm[1] < guideImage.getHeight() 
                                    && visited[tempI + prm[0]][tempJ + prm[1]] == 0 
                                    && getBlue(analysisImage.getRGB(tempI + prm[0], tempJ + prm[1])) > thresh) {
                                visited[tempI + prm[0]][tempJ + prm[1]] = currentGrp;
                                q.add((tempI + prm[0]) * guideImage.getHeight() + (tempJ + prm[1]));
                                if(getBlue(guideImage.getRGB(tempI + prm[0], tempJ + prm[1])) > gThresh) {
                                    ct++;
                                }
                            }
                        }
                    }
                    
                    values.add(ct);
                    currentGrp ++;
                }
            }
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Image Analyzer");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(102, 204, 0));
        jButton1.setText("Analyze Image");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(50, 125, 25));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Guide Uploaded: No");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Analysis Uploaded: No");

        jButton2.setText("Upload Guide Image");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Upload Analysis Image");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("System Log");

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(50, 75, 125));
        jTextArea1.setColumns(15);
        jTextArea1.setFont(new java.awt.Font("Consolas", 1, 10)); // NOI18N
        jTextArea1.setForeground(new java.awt.Color(200, 200, 200));
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jButton4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(200, 50, 50));
        jButton4.setText("Clear Analysis");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jSeparator1.setBackground(new java.awt.Color(240, 240, 240));

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel3.setText("Threshold (leave blank if automatic):");

        jLabel8.setText("Engine Version 0.20");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton1)
                        .addComponent(jButton4))
                    .addComponent(jSeparator2)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Method to analyze image when button is pressed
    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // Check if images actually have been loaded
        if(gThresh != -1 && thresh != -1) {
            // Check if images are the same size
            if(guideImage.getWidth() != analysisImage.getWidth() || 
                guideImage.getHeight() != analysisImage.getHeight()) {
                jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Error: Dimension mismatch. Could not analyse.");
            }
            else {
                // Mark all neuron pixels in the guide image
                scanImages();
                
                jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Summing groups...");
                
                // Construct map of images
                double[][] groups = new double[currentGrp - 1][4];
                ArrayList<Integer> listLarge = new ArrayList<>();
                BufferedImage a = new BufferedImage(guideImage.getWidth(), guideImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                
                for(int i = 0; i < guideImage.getWidth(); i++) {
                    for(int j = 0; j < guideImage.getHeight(); j++) {
                        // Check whether a relevant astrocyte has been detected
                        if(visited[i][j] != 0) {
                            // Record data
                            double[] tempInfo = groups[visited[i][j] - 1];
                            tempInfo[0] ++;
                            tempInfo[1] += getBlue(analysisImage.getRGB(i, j));
                            groups[visited[i][j] - 1] = tempInfo;
                        }
                        else {
                            a.setRGB(i, j, 0);
                        }
                    }
                }
                
                // Check if nucleus size is over threshold
                for(int i = 0; i < groups.length; i++) {
                    groups[i][3] = values.poll();
                    if(groups[i][0] > MIN_NUCLEUS_SIZE && groups[i][3] > MIN_ASTROCYTE_SIZE) {
                        listLarge.add(i);
                    }
                }
                
                // Draw map
                for(int i = 0; i < guideImage.getWidth(); i++) {
                    for(int j = 0; j < guideImage.getHeight(); j++) {
                        // Mark neuron detected
                        if(getBlue(guideImage.getRGB(i, j)) > gThresh) {
                            Color c = new Color(125, 0, 255, 255);
                            a.setRGB(i, j, c.getRGB());
                        }
                        if(visited[i][j] != 0 && listLarge.contains(visited[i][j] - 1)) {
                            // Color portion of relevant astrocyte
                            int decide = listLarge.indexOf(visited[i][j] - 1) + 1;
                            int redv = decide & 3;
                            int greenv = (decide & 12)/4;
                            Color c = new Color(redv*80, greenv*80, 0, 255);
                            Color neuron = new Color(125, 0, 255, 255);
                            if(a.getRGB(i, j) != neuron.getRGB()) {
                                a.setRGB(i, j, c.getRGB());
                            }
                            else {
                                c = new Color(redv*80, greenv*80, 255, 255);
                                a.setRGB(i, j, c.getRGB());
                            }
                        }
                    }
                }
                
                
                // Log and write images and data to files
                jTextArea1.setText(jTextArea1.getText() + "\n\n#  | Pixels         | MeanInt        | RawInt         "
                        + "\n------------------------------------------------------");
                try {
                    PrintWriter data = new PrintWriter(new FileWriter(username + "/Documents/AnalysisEngine/data/data" + count + ".txt"));
                    PrintWriter cumulative = new PrintWriter(new FileWriter(username + "/Documents/AnalysisEngine/cumulative.csv", true));
                    
                    data.println("Pixels      MeanInt     RawInt");
                    cumulative.println("Run #" + count + ", File: " + path + ",");
                    cumulative.println("Size,MeanIntensity,RawIntensity");
                    for(int i = 0; i < groups.length; i++) {
                        double[] grp = groups[i];
                        
                        if(listLarge.contains(i)) {
                            // Calculate mean
                            grp[2] = grp[1]/grp[0];
                            
                            // Add the count of each group to cumulative data
                            cumulative.println((Math.floor(grp[0]*1000.0/625.0)/1000) + "," + (Math.floor(grp[2]*1000)/1000) + "," + Math.floor(grp[1]));
                            StringBuilder s = new StringBuilder("" + Math.floor(grp[0]) + "/" + grp[3]);
                            StringBuilder s2 = new StringBuilder("" + Math.floor(grp[2]*1000)/1000);
                            StringBuilder s3 = new StringBuilder("" + Math.floor(grp[1]));
                        
                            // Pad with whitespace
                            while(s.length() < 16) {
                                s.append(" ");
                            }
                            while(s2.length() < 16) {
                                s2.append(" ");
                            }
                            while(s3.length() < 16) {
                                s3.append(" ");
                            }
                            
                            int decide = listLarge.indexOf(i) + 1;
                            int redv = decide & 3;
                            int greenv = (decide & 12)/4;
                            
                            StringBuilder s4 = new StringBuilder("" + (decide - 1));
                            while(s4.length() < 3) {
                                s4.append(" ");
                            }
                            
                            jTextArea1.setText(jTextArea1.getText() + "\n" + s4.toString() + "|" 
                                    + s.toString() + "|" + s2.toString() + "|" + s3.toString() 
                                    + "\n------------------------------------------------------");
                            data.println(s.toString() + s2.toString() + s3.toString() + 
                                    " RGB(" + redv*80 + ", " + greenv*80 + ", 0)");
                        }
                    }
                    
                    // Write final data and close files
                    cumulative.println("Size: " + listLarge.size() + ",,");
                    data.println(listLarge.size());
                    data.close();
                    cumulative.close();
                    
                    // Change to lossless format
                    File outputfile = new File(username + "/Documents/AnalysisEngine/maps/result" + count + ".png");
                    ImageIO.write(a, "png", outputfile);
                    count++;
                    PrintWriter out = new PrintWriter(new FileWriter(username + "/Documents/AnalysisEngine/count/count.txt"));
                    out.println(count);
                    out.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                jTextArea1.setText(jTextArea1.getText() + "\n" +"Complete!");
            }
        }
        else {
            jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Error: Files not complete. Could not analyse.");
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Handle open button action.
        int returnVal = fc.showOpenDialog(ANIMA.this);

        // If file opened, read in image
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            
            // Check if the file exists
            if(file.exists()) {
                path = file.getAbsolutePath();
                try {
                    jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Processing Image...");
                    
                    // Read image
                    FileInputStream in = new FileInputStream(file);
                    guideImage = ImageIO.read(in);
                    
                    in.close();
                    
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Calculating Threshold...");
                    
                    double sum = 0;
                    
                    for(int i = 0; i < guideImage.getWidth(); i++) {
                        for(int j = 0; j < guideImage.getHeight(); j++) {
                            sum += getBlue(guideImage.getRGB(i, j));
                        }
                    }
                
                    double mean = sum / ((double) guideImage.getHeight()*guideImage.getWidth());
                
                    double stdev = 0;
                    for(int i = 0; i < guideImage.getWidth(); i++) {
                        for(int j = 0; j < guideImage.getHeight(); j++) {
                            stdev += Math.pow(getBlue(guideImage.getRGB(i, j)) - mean, 2);
                        }
                    }
                
                
                    stdev = stdev / ((double) guideImage.getHeight()*guideImage.getWidth());
                    stdev = Math.sqrt(stdev);
                    
                    // Calculate guide threshold
                    gThresh = (int) (mean + stdev/ GUIDE_DEVIATION);
                    
                    
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Status: Ready");
                    //jLabel4.setForeground(new Color(50, 125, 25));
                    jLabel5.setText("Guide Uploaded: Yes");
                    jLabel5.setForeground(new Color(50, 125, 25));
                
                    
                } catch(IOException e) {
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Error: Upload failed. Try again.");
                    //jLabel4.setForeground(Color.RED);
                }
            }
        } else {
            jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Open command cancelled by user.");
            //jLabel4.setForeground(Color.ORANGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        int returnval = fc.showOpenDialog(ANIMA.this);
        if(returnval == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if(f.exists()) {
                try {
                    jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Processing Image...");
                
                    FileInputStream in = new FileInputStream(f);
                    analysisImage = ImageIO.read(in);
                
                    in.close();
               
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Calculating Threshold...");
                
                
                    double sum = 0;
                
                    for(int i = 0; i < analysisImage.getWidth(); i++) {
                        for(int j = 0; j < analysisImage.getHeight(); j++) {
                            sum += getBlue(analysisImage.getRGB(i, j));
                        }
                    }
                
                    double mean = sum / ((double) analysisImage.getHeight()*analysisImage.getWidth());
                
                    double stdev = 0;
                    for(int i = 0; i < analysisImage.getWidth(); i++) {
                        for(int j = 0; j < analysisImage.getHeight(); j++) {
                            stdev += Math.pow(getBlue(analysisImage.getRGB(i, j)) - mean, 2);
                        }
                    }
                
                
                    stdev = stdev / ((double) analysisImage.getHeight()*analysisImage.getWidth());
                    stdev = Math.sqrt(stdev);
                    
                    String s = jTextField1.getText();
                    if(s.isEmpty()) {
                        // Use automatic threshold
                        thresh = (int) (mean + stdev/ANALYSIS_DEVIATION);
                    
                        if(thresh < 5) {
                            thresh = 5;
                        }
                    
                        jTextArea1.setText(jTextArea1.getText() + "\n" +"Status: Ready");
                        jLabel6.setText("Analysis Uploaded: Yes");
                        jLabel6.setForeground(new Color(50, 125, 25));
                    }
                    else {
                        // Set a manual threshold
                        int num = Integer.parseInt(s);
                        
                        if(num < 0 || num > 255) {
                            jTextArea1.setText(jTextArea1.getText() + "\n" + "Warning: Threshold out of bounds. Rectifying.");
                            num = Math.min(Math.max(num, 0), 255);
                        }
                        
                        thresh = num;
                        
                        jTextArea1.setText(jTextArea1.getText() + "\n" +"Status: Ready");
                        jLabel6.setText("Analysis Uploaded: Yes");
                        jLabel6.setForeground(new Color(50, 125, 25));
                    }

                } catch(IOException e) {
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Error: Upload failed. Try again.");
                } catch(NumberFormatException e) {
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Error: Non-integer entered as threshold. Try again.");
                }
            }
        }
        else {
            jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Open command cancelled by user.");
            //jLabel4.setForeground(Color.ORANGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        
        guideImage = null;
        analysisImage = null;
        thresh = -1;
        gThresh = -1;
        
        visited = new int[0][0];
        currentGrp = 1;
        values = new ArrayDeque<>();
        path = "";
        
        jLabel5.setText("Guide Uploaded: No");
        jLabel5.setForeground(Color.BLACK);
        jLabel6.setText("Analysis Uploaded: No");
        jLabel6.setForeground(Color.BLACK);
        jTextArea1.setText(jTextArea1.getText() + "\n-------------------\n\nImage Analyzer - Run #" + count);
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ANIMA.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ANIMA().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
