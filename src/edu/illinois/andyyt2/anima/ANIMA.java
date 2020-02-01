/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.illinois.andyyt2.anima;

import java.awt.Color;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;



/**
 *
 * @author " + username + "
 */
public class ANIMA extends javax.swing.JFrame {

    private BufferedImage guideImage;
    private BufferedImage analysisImage;
    private int thresh;
    private int gThresh;
    private int[][] visited;
    private int currentGrp;
    private int count;
    private final JFileChooser fc = new JFileChooser();
    private double stdivide = 1.0;
    private double stdivide2 = 4.0;
    private String username;
    //private String header = "C:/Users/" + username + "/Desktop/KMC Lab/CA3_KMC_TIFF/052419_KMC_F-1_4 month_IV-Lt-3_Ca3/";

    /**
     * Creates new form ANIMA
     */
    public ANIMA() {
        initComponents();
        guideImage = null;
        analysisImage = null;
        thresh = -1;
        gThresh = -1;
        username = System.getProperty("user.home");
        
        visited = new int[0][0];
        currentGrp = 1;
        
        BufferedReader f;
        try {
            System.out.println(username);
            boolean directory = new File(username + "/AnalysisEngine").mkdirs();
            if(!directory) {
                boolean count = new File(username + "/AnalysisEngine/count").mkdirs();
                boolean maps = new File(username + "/AnalysisEngine/maps").mkdirs();

                System.out.println("Directory created? ");
                System.out.println("count: " + count);
                System.out.println("maps: " + maps);
            }

            boolean exists = new File(username + "/AnalysisEngine/count/count.txt").isFile();
            if(!exists) {
                PrintWriter out = new PrintWriter(new FileWriter(username + "/AnalysisEngine/count/count.txt"));
                out.println(0);
                out.close();
            }

            f = new BufferedReader(new FileReader(username + "/AnalysisEngine/count/count.txt"));
            count = Integer.parseInt(f.readLine());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ANIMA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ANIMA.class.getName()).log(Level.SEVERE, null, ex);
            count = 10000;
        }
        
        jTextArea1.setText("Image Analyzer - Run #" + count);
        jLabel2.setText("Output to C:/Users/" + username + "/Documents/AnalysisEngine");
    }
    
    private int getBlue(int a) {
        Color c = new Color(a);
        return c.getBlue();
    }
    
    private void scanGuide() {
        jTextArea1.setText(jTextArea1.getText() + "\n" +"Scanning guide (may take a while)...");
        //jLabel4.setForeground(Color.ORANGE);
        //BFS
        visited = new int[guideImage.getWidth()][guideImage.getHeight()];
        currentGrp = 1;
        Queue<Integer> q = new ArrayDeque<>();
        for(int i = 0; i < guideImage.getWidth(); i++) {
            for(int j = 0; j < guideImage.getHeight(); j++) {
                //System.out.println(i + " " + j);
                //System.out.println(thresh + " " + gThresh);
                if(getBlue(guideImage.getRGB(i, j)) > gThresh 
                        && getBlue(analysisImage.getRGB(i, j)) > thresh 
                        && visited[i][j] == 0) {
                    visited[i][j] = currentGrp;
                    q.add(i*guideImage.getHeight() + j);
                    
                    while(!q.isEmpty()) {
                        int hash = q.poll();
                        int tempI = hash / guideImage.getHeight();
                        int tempJ = hash % guideImage.getHeight();
                        
                        int[][] permute = {{-1, -1}, {-1, 0}, {-1, 1}, 
                        {0, -1}, {0, 0}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
                        
                        for (int[] prm : permute) {
                            //System.out.println(Arrays.toString(prm));
                            if (tempI + prm[0] >= 0 
                                    && tempI + prm[0] < guideImage.getWidth() 
                                    && tempJ + prm[1] >= 0 
                                    && tempJ + prm[1] < guideImage.getHeight() 
                                    && visited[tempI + prm[0]][tempJ + prm[1]] == 0 
                                    && getBlue(analysisImage.getRGB(tempI + prm[0], tempJ + prm[1])) > thresh) {
                                visited[tempI + prm[0]][tempJ + prm[1]] = currentGrp;
                                q.add((tempI + prm[0]) * guideImage.getHeight() + (tempJ + prm[1]));
                            }
                        }
                    }
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
        jLabel2 = new javax.swing.JLabel();

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

        jLabel2.setText("Output to C:/Users/[username]/Documents/AnalysisEngine");

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
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addComponent(jLabel2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        // TODO add your handling code here:
        if(gThresh != -1 && thresh != -1) {
            if(guideImage.getWidth() != analysisImage.getWidth() || 
                guideImage.getHeight() != analysisImage.getHeight()) {
                jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Error: Dimension mismatch. Could not analyse.");
                //jLabel4.setForeground(Color.RED);
            }
            else {
                scanGuide();
                jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Summing groups...");
                //jLabel4.setForeground(Color.ORANGE);
                double[][] groups = new double[currentGrp - 1][3];
                ArrayList<Integer> listLarge = new ArrayList<>();
                BufferedImage a = new BufferedImage(guideImage.getWidth(), guideImage.getHeight(), BufferedImage.TYPE_INT_RGB);
                
                for(int i = 0; i < guideImage.getWidth(); i++) {
                    for(int j = 0; j < guideImage.getHeight(); j++) {
                        if(visited[i][j] != 0) {
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
                
                for(int i = 0; i < groups.length; i++) {
                    if(groups[i][0] > 500.0) {
                        listLarge.add(i);
                    }
                }
                
                for(int i = 0; i < guideImage.getWidth(); i++) {
                    for(int j = 0; j < guideImage.getHeight(); j++) {
                        if(visited[i][j] != 0 && listLarge.contains(visited[i][j] - 1)) {
                            int decide = listLarge.indexOf(visited[i][j] - 1) + 1;
                            int redv = decide & 3;
                            int greenv = (decide & 12)/4;
                            Color c = new Color(redv*80, greenv*80, 0, 255);
                            a.setRGB(i, j, c.getRGB());
                        }
                    }
                }
                
                
                
                jTextArea1.setText(jTextArea1.getText() + "\n\n#  | Pixels         | MeanInt        | RawInt         "
                        + "\n------------------------------------------------------");
                //jLabel4.setForeground(Color.ORANGE);
                try {
                    PrintWriter data = new PrintWriter(new FileWriter("C:/Users/" + username + "/Documents/AnalysisEngine/data/data" + count + ".txt"));
                    PrintWriter cumulative = new PrintWriter(new FileWriter("C:/Users/" + username + "/Documents/AnalysisEngine/cumulative.csv", true));
                    
                    data.println("Pixels      MeanInt     RawInt");
                    cumulative.println("Run #" + count + ",,");
                    cumulative.println("Size,MeanIntensity,RawIntensity");
                    System.out.println("Pixels      MeanInt     RawInt");
                    for(int i = 0; i < groups.length; i++) {
                        double[] grp = groups[i];
                        
                        if(listLarge.contains(i)) {
                            //Mean
                            grp[2] = grp[1]/grp[0];
                            cumulative.println((Math.floor(grp[0]*1000.0/625.0)/1000) + "," + (Math.floor(grp[2]*1000)/1000) + "," + Math.floor(grp[1]));
                            StringBuilder s = new StringBuilder("" + Math.floor(grp[0]));
                            StringBuilder s2 = new StringBuilder("" + Math.floor(grp[2]*1000)/1000);
                            StringBuilder s3 = new StringBuilder("" + Math.floor(grp[1]));
                        
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
                            System.out.println(s.toString() + s2.toString() + s3.toString());
                        }
                    }
                    cumulative.println("Size: " + listLarge.size() + ",,");
                    data.println(listLarge.size());
                    data.close();
                    cumulative.close();
                    
                    File outputfile = new File("C:/Users/" + username + "/Documents/AnalysisEngine/maps/result" + count + ".jpg");
                    ImageIO.write(a, "jpg", outputfile);
                    count++;
                    System.out.println(count);
                    PrintWriter out = new PrintWriter(new FileWriter("C:/Users/" + username + "/Documents/AnalysisEngine/count/count.txt"));
                    out.println(count);
                    out.close();
                    
                } catch (IOException e) {
                    //Stuff should happen lol
                }
                
                jTextArea1.setText(jTextArea1.getText() + "\n" +"Complete!");
                //jLabel4.setForeground(new Color(50, 125, 25));
            }
        }
        else {
            jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Error: Files not complete. Could not analyse.");
            //jLabel4.setForeground(Color.RED);
        }
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        //Handle open button action.
        int returnVal = fc.showOpenDialog(ANIMA.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            if(file.exists()) {
                try {
                    jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Processing Image...");
                    //jLabel4.setForeground(Color.ORANGE);
                    
                    FileInputStream in = new FileInputStream(file);
                    guideImage = ImageIO.read(in);
                    
                    in.close();
                    
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Calculating Threshold...");
                    //jLabel4.setForeground(Color.ORANGE);
                    
                    
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
                    
                    //TODO: CHANGE [DONE]
                    System.out.println("Guide threshold:" + (mean + stdev/stdivide));
                    gThresh = (int) (mean + stdev/stdivide);
                    
                    
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
        // TODO add your handling code here:
        int returnval = fc.showOpenDialog(ANIMA.this);
        if(returnval == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if(f.exists()) {
                try {
                    
                    jTextArea1.setText(jTextArea1.getText() + "\n\n" +"Processing Image...");
                    //(Color.ORANGE);
                
                    FileInputStream in = new FileInputStream(f);
                    analysisImage = ImageIO.read(in);
                
                    in.close();
               
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Calculating Threshold...");
                    //jLabel4.setForeground(Color.ORANGE);
                
                
                    double sum = 0;
                
                    for(int i = 0; i < analysisImage.getWidth(); i++) {
                        for(int j = 0; j < analysisImage.getHeight(); j++) {
                            if(j % 10 == 0) {
                                //System.out.println(getBlue(analysisImage.getRGB(i, j)));
                            }
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
                    System.out.println(mean + " " + stdev);
                    
                    //TODO: CHANGE [DONE]
                    System.out.println("Analysis threshold:" + (mean + stdev/stdivide2));
                    thresh = (int) (mean + stdev/stdivide2);
                    if(thresh < 5) {
                        thresh = 5;
                    }
                    //thresh = 15;
                
                    //thresh = 35;
                
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Status: Ready");
                    //jLabel4.setForeground(new Color(50, 125, 25));
                    jLabel6.setText("Analysis Uploaded: Yes");
                    jLabel6.setForeground(new Color(50, 125, 25));
                

                } catch(IOException e) {
                    jTextArea1.setText(jTextArea1.getText() + "\n" +"Error: Upload failed. Try again.");
                    //jLabel4.setForeground(Color.RED);
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
