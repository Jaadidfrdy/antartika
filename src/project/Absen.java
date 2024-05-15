/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package project;

import javax.swing.JOptionPane;
import konek.koneksi;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Window;
import java.sql.*;
import java.sql.Connection;
import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author dika2
 */
public class Absen extends javax.swing.JFrame {
    public static Connection cn;
    public static ResultSet rs;
    public static Statement st;
    public static PreparedStatement pst;
    DefaultTableModel model;
    /**
     * Creates new form Absen
     */
    public Absen() {
        initComponents();
        autonomer();
        RealTimeDate();
        datatable();
        model = (DefaultTableModel) DataTable.getModel();
        // Set tanggal default ke JDateChooser
        dateDari.setDate(new Date());
        dateSampai.setDate(new Date());
        datatablecari();
        setExtendedState(Absen.MAXIMIZED_BOTH);
    }
    
    public void close_all(){
        if (pst != null){
            try{
                pst.close();
            }catch(SQLException ex){
                //ignore
            }
            if (cn != null)
                try {
                    cn.close();
                } catch (SQLException ex) {
                    // Ignore
                }
            if (st != null)
                try {
                    st.close();
                } catch (SQLException ex) {
                    // Ignore
                }
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException ex) {
                    // Ignore
                }
        } else {
        }
    }
    
    private void clear(){
        txNis.setText("");
        txNama.setText("");
        txKelas.setText("");
//        dateDari.setDate(null);
//        dateSampai.setDate(null);
    }
    
    private void autonomer(){
        String sql = "select max(right(no_absen,1)) from absensi_siswa";
        try{
            Statement s = koneksi.GetConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = s.executeQuery(sql);
            while(rs.next()){
                if(rs.first()==false){
                    noAbsen.setText("ABS001");
                }else{
                    rs.last();
                    int no = rs.getInt(1)+1;
                    String number = String.valueOf(no);
                    int panjang_no = number.length();
                    for ( int i = 0 ; i<2-panjang_no ; i++){
                    number = "00" + number;
                    }
                noAbsen.setText("ABS"+number);
                }
            }
        }catch (Exception e){
            System.out.println(""+e.getMessage());
        }
        noAbsen.setEnabled(false);
    }
    
    public void RealTimeDate(){
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = format.format(now);
        txTanggal.setText(tanggal);
        
        txTanggal.setEnabled(false);
    }
    
    public void datatable(){
        DefaultTableModel tbl = new DefaultTableModel();
        tbl.addColumn("NO ABSEN");
        tbl.addColumn("NIS");
        tbl.addColumn("NAMA");
        tbl.addColumn("KELAS");
        tbl.addColumn("TANGGAL");
        DataTable.setModel(tbl);
        try{
            Statement statement = (Statement)koneksi.GetConnection().createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM absensi_siswa");
            
            while (res.next())
            {
                tbl.addRow(new Object[]{
                    res.getString("no_absen"),
                    res.getString("nis"),
                    res.getString("nama_siswa"),
                    res.getString("kelas"),
                    res.getString("tanggal"),
                });
                DataTable.setModel(tbl);
            }
        }catch (SQLException e){
                    JOptionPane.showMessageDialog(rootPane, "salah"+e.getMessage() );
                    }
    }
    
    public void datatablecari(){
        DefaultTableModel modelDataTable1 = new DefaultTableModel();
        modelDataTable1.addColumn("NO ABSEN");
        modelDataTable1.addColumn("NIS");
        modelDataTable1.addColumn("NAMA");
        modelDataTable1.addColumn("KELAS");
        modelDataTable1.addColumn("TANGGAL");

        // Set model tabel setelah selesai mengambil data
        DataTable1.setModel(modelDataTable1);
    }
    
    public void absen(){
        if (noAbsen.getText().isEmpty() || txNis.getText().isEmpty()
                || txNama.getText().isEmpty() || txKelas.getText().isEmpty()
                || txTanggal.getText().isEmpty()) {
            // Show an error message
            JOptionPane.showMessageDialog(this, "Semua data harus di isi");
            return;
        }
        try{
                String sql = "INSERT INTO absensi_siswa (no_absen, nis, nama_siswa, kelas, tanggal) VALUES(?, ?, ?, ?, ?)"; 
                cn = koneksi.GetConnection();
                pst = cn.prepareStatement(sql);
             
                pst.setString(1, noAbsen.getText());
                pst.setString(2, txNis.getText());
                pst.setString(3,txNama.getText());
                pst.setString(4, txKelas.getText());
                // Gunakan SimpleDateFormat untuk mengubah format tanggal
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                // Parse tanggal dari string ke tipe Date
                java.util.Date parsedDate = dateFormat.parse(txTanggal.getText());
                // Set tanggal pada PreparedStatement
                pst.setDate(5, new java.sql.Date(parsedDate.getTime()));
                
                pst.execute();
                JOptionPane.showMessageDialog(this, "Absen Berhasil");
                
                }
            catch(Exception e){
              JOptionPane.showMessageDialog(this, "Absen Gagal " + e.getMessage());
              e.printStackTrace();
           }finally{
            close_all();
        }
        clear();
        datatable();
        
//        // Initialize the model for DataTable
//        DefaultTableModel model = new DefaultTableModel();
//        model.addColumn("NO ABSEN");
//        model.addColumn("NIS");
//        model.addColumn("NAMA");
//        model.addColumn("KELAS");
//        model.addColumn("TANGGAL");
//
//        // Initialize the model for DataTable1
//        DefaultTableModel model1 = new DefaultTableModel();
//        model1.addColumn("NO ABSEN");
//        model1.addColumn("NIS");
//        model1.addColumn("NAMA");
//        model1.addColumn("KELAS");
//        model1.addColumn("TANGGAL");
//
//        // Gunakan SimpleDateFormat untuk mengubah format tanggal
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        String query = "SELECT * FROM absensiswa WHERE tanggal BETWEEN ? AND ?";
//
//        try {
//            if (dateDari.getDate() != null && dateSampai.getDate() != null) {
//                Connection con = conek.GetConnection();
//                PreparedStatement stt = con.prepareStatement(query);
//
//                // Set the date parameters
//                stt.setString(1, dateFormat.format(dateDari.getDate()));
//                stt.setString(2, dateFormat.format(dateSampai.getDate()));
//
//                ResultSet rslt = stt.executeQuery();
//
//                while (rslt.next()) {
//                    String nomor = rslt.getString("no_absen");
//                    String nis = rslt.getString("nis");
//                    String nama = rslt.getString("nama_siswa");
//                    String kelas = rslt.getString("kelas");
//                    String tanggal = rslt.getString("tanggal");
//
//                    // Add the row to the model for DataTable
//                    String[] data = {String.valueOf(nomor), String.valueOf(nis), String.valueOf(nama), String.valueOf(kelas), String.valueOf(tanggal), ""};
//                    model.addRow(data);
//
//                    // Add the row to the model for DataTable1
//                    model1.addRow(data);
//                }
//
//                // Set model tabel setelah selesai mengambil data
//                DataTable.setModel(model);
//                DataTable1.setModel(model1);
//
//                // Tutup result set dan prepared statement
//                close_all();
//            } else {
//                // Lakukan penanganan kesalahan atau tindakan lain sesuai kebutuhan
//                System.out.println("dateDari atau dateSampai memiliki nilai null.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace(); // Log the exception for debugging
//        }
    }
    
    public void caridata(){
        // Gunakan SimpleDateFormat untuk mengubah format tanggal
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String query = "SELECT * FROM absensi_siswa WHERE CAST(tanggal AS DATE) BETWEEN ? AND ?";

        try {
            // Pastikan kedua tanggal telah dipilih
            if (dateDari.getDate() == null || dateSampai.getDate() == null) {
//                JOptionPane.showMessageDialog(this, "Pilih tanggal dari dan sampai terlebih dahulu.");
                return; // Keluar dari metode jika salah satu tanggal belum dipilih
            }

            Connection con = koneksi.GetConnection();
            PreparedStatement stt = con.prepareStatement(query);

            // Set the date parameters
            stt.setString(1, dateFormat.format(dateDari.getDate()));
            stt.setString(2, dateFormat.format(dateSampai.getDate()));

            ResultSet rslt = stt.executeQuery();

            // Melakukan iterasi melalui hasil query dan menambahkannya ke model tabel
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("NO ABSEN");
            model.addColumn("NIS");
            model.addColumn("NAMA");
            model.addColumn("KELAS");
            model.addColumn("TANGGAL");

            while (rslt.next()) {
                model.addRow(new Object[]{
                    rslt.getString("no_absen"),
                    rslt.getString("nis"),
                    rslt.getString("nama_siswa"),
                    rslt.getString("kelas"),
                    rslt.getString("tanggal")
                });
            }
            int baris = model.getRowCount();
            for(int a = 0; a < baris; a++){
                String no = String.valueOf(a + 1);
                
            }
            
            txtTotal.setText(String.valueOf(baris));

            // Set model tabel setelah selesai mengambil data
            DataTable1.setModel(model);

            // Tutup result set dan prepared statement
            rslt.close();
            stt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception for debugging
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

        txTanggal = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        DataTable1 = new javax.swing.JTable();
        btAbsen = new javax.swing.JButton();
        txKelas = new javax.swing.JTextField();
        txNama = new javax.swing.JTextField();
        txNis = new javax.swing.JTextField();
        noAbsen = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DataTable = new javax.swing.JTable();
        btTampil = new javax.swing.JButton();
        dateSampai = new com.toedter.calendar.JDateChooser();
        dateDari = new com.toedter.calendar.JDateChooser();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTotal = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txTanggal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        getContentPane().add(txTanggal, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 410, 120, -1));

        jButton1.setText("dashboard");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(49, 86, -1, -1));

        jButton2.setText("data buku");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, -1, -1));

        jButton3.setText("pinjaman");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, -1, -1));

        DataTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(DataTable1);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 450, 810, 540));

        btAbsen.setText("ABSEN");
        btAbsen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAbsenActionPerformed(evt);
            }
        });
        getContentPane().add(btAbsen, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 370, -1, -1));
        getContentPane().add(txKelas, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 370, 242, 30));
        getContentPane().add(txNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 330, 242, 30));
        getContentPane().add(txNis, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 290, 242, 30));

        noAbsen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        noAbsen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noAbsenActionPerformed(evt);
            }
        });
        getContentPane().add(noAbsen, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 240, 70, -1));

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("Nomor");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("NIS");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 310, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("NAMA");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 340, -1, -1));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("KELAS");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 380, -1, -1));

        DataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(DataTable);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 446, 850, 540));

        btTampil.setText("TAMPIL");
        btTampil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btTampilActionPerformed(evt);
            }
        });
        getContentPane().add(btTampil, new org.netbeans.lib.awtextra.AbsoluteConstraints(1100, 420, 810, -1));
        getContentPane().add(dateSampai, new org.netbeans.lib.awtextra.AbsoluteConstraints(1600, 390, 310, -1));
        getContentPane().add(dateDari, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 390, 310, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("SAMPAI");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1510, 390, 60, 26));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("DARI");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 390, 40, 26));

        txtTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        txtTotal.setForeground(new java.awt.Color(0, 0, 0));
        txtTotal.setText("0");
        getContentPane().add(txtTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(1860, 990, -1, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("TOTAL JUMLAH PENGUNJUNG YANG HADIR  :");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(1530, 990, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/absen.png"))); // NOI18N
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1920, 1080));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btAbsenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAbsenActionPerformed
        // TODO add your handling code here:
        absen();
    }//GEN-LAST:event_btAbsenActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
new dashboard().show();
                dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
new DataBuku().show();
                dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
new pinjaman().show();
                dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void noAbsenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noAbsenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noAbsenActionPerformed

    private void btTampilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btTampilActionPerformed
        if (dateDari.getDate() == null || dateSampai.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Pilih tanggal dari dan sampai terlebih dahulu.");
            return; // Keluar dari metode jika salah satu tanggal belum dipilih
        }

        // Panggil metode caridata() setelah kedua tanggal dipilih
        caridata();
    }//GEN-LAST:event_btTampilActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Absen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Absen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Absen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Absen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Absen().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable DataTable;
    private javax.swing.JTable DataTable1;
    private javax.swing.JButton btAbsen;
    private javax.swing.JButton btTampil;
    private com.toedter.calendar.JDateChooser dateDari;
    private com.toedter.calendar.JDateChooser dateSampai;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField noAbsen;
    private javax.swing.JTextField txKelas;
    private javax.swing.JTextField txNama;
    private javax.swing.JTextField txNis;
    private javax.swing.JTextField txTanggal;
    private javax.swing.JLabel txtTotal;
    // End of variables declaration//GEN-END:variables
}
