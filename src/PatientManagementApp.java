import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PatientManagementApp extends JFrame {

    private final List<Patient> patients;
    private int currentPatientIndex;

    private JTextField txtName;
    private JTextField txtAddress;
    private JTextField txtNik;
    private JTextField txtDateOfBirth;
    private JTable tblPatients;

    public PatientManagementApp() {
        patients = new ArrayList<>();
        currentPatientIndex = -1;

        setTitle("Patient Management App");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createFormPanel();
        createButtonPanel();
        createTablePanel();

        setVisible(true);
    }

    private void createFormPanel() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblName = new JLabel("Nama Pasien:");
        txtName = new JTextField(20);
        JLabel lblAddress = new JLabel("Alamat:");
        txtAddress = new JTextField(50);
        JLabel lblNik = new JLabel("NIK:");
        txtNik = new JTextField(15);
        JLabel lblDateOfBirth = new JLabel("Tanggal Lahir (YYYY-MM-DD):");
        txtDateOfBirth = new JTextField(10);

        formPanel.add(lblName);
        formPanel.add(txtName);
        formPanel.add(lblAddress);
        formPanel.add(txtAddress);
        formPanel.add(lblNik);
        formPanel.add(txtNik);
        formPanel.add(lblDateOfBirth);
        formPanel.add(txtDateOfBirth);

        add(formPanel, BorderLayout.NORTH);
    }

    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnAdd = new JButton("Tambah");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Hapus");
        JButton btnPrevious = new JButton("Sebelumnya");
        JButton btnNext = new JButton("Berikutnya");
        JButton btnList = new JButton("Daftar Pasien");
        JButton btnExit = new JButton("Keluar");

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatePatient();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePatient();
            }
        });

        btnPrevious.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigatePrevious();
            }
        });

        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigateNext();
            }
        });

        btnList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showPatientList();
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnPrevious);
        buttonPanel.add(btnNext);
        buttonPanel.add(btnList);
        buttonPanel.add(btnExit);

        add(buttonPanel, BorderLayout.SOUTH); // Menambahkan panel tombol ke JFrame
        setVisible(true); // Menampilkan JFrame
    }

    private void createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnNames = {"No", "Nama Pasien", "NIK", "Tanggal Lahir", "Alamat"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        tblPatients = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(tblPatients);

        tablePanel.add(scrollPane);

        add(tablePanel, BorderLayout.SOUTH);
    }

    private void addPatient() {
        String name = txtName.getText();
        String address = txtAddress.getText();
        String nik = txtNik.getText();
        String dateOfBirthString = txtDateOfBirth.getText();

        if (name.isEmpty() || address.isEmpty() || nik.isEmpty() || dateOfBirthString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap lengkapi semua field!");
            return;
        }

        if (!isUniqueNik(nik)) {
            JOptionPane.showMessageDialog(this, "No. NIK sudah terdaftar!");
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = dateFormat.parse(dateOfBirthString);

            Patient patient = new Patient(name, address, nik, dateOfBirth);
            patients.add(patient);
            updateTable();

            clearForm();
            JOptionPane.showMessageDialog(this, "Data pasien berhasil ditambahkan!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format tanggal lahir tidak valid!");
        }
    }

    private void updatePatient() {
        if (currentPatientIndex == -1) {
            JOptionPane.showMessageDialog(this, "Tidak ada data pasien yang dipilih!");
            return;
        }

        String name = txtName.getText();
        String address = txtAddress.getText();
        String nik = txtNik.getText();
        String dateOfBirthString = txtDateOfBirth.getText();

        if (name.isEmpty() || address.isEmpty() || nik.isEmpty() || dateOfBirthString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Harap lengkapi semua field!");
            return;
        }

        if (!isUniqueNik(nik, currentPatientIndex)) {
            JOptionPane.showMessageDialog(this, "No. NIK sudah terdaftar!");
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dateOfBirth = dateFormat.parse(dateOfBirthString);

            Patient patient = patients.get(currentPatientIndex);
            patient.setName(name);
            patient.setAddress(address);
            patient.setNik(nik);
            patient.setDateOfBirth(dateOfBirth);
            updateTable();

            clearForm();
            JOptionPane.showMessageDialog(this, "Data pasien berhasil diperbarui!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Format tanggal lahir tidak valid!");
        }
    }

    private void deletePatient() {
        if (currentPatientIndex == -1) {
            JOptionPane.showMessageDialog(this, "Tidak ada data pasien yang dipilih!");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this, "Anda yakin ingin menghapus data pasien ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            patients.remove(currentPatientIndex);
            currentPatientIndex = -1;
            updateTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Data pasien berhasil dihapus!");
        }
    }

    private void navigatePrevious() {
        if (currentPatientIndex > 0) {
            currentPatientIndex--;
            displayCurrentPatient();
        }
    }

    private void navigateNext() {
        if (currentPatientIndex < patients.size() - 1) {
            currentPatientIndex++;
            displayCurrentPatient();
        }
//        System.out.println("OutcurrentPatientIndex: " + currentPatientIndex);
//        System.out.println("patients.size(): " + patients.size());
    }

    private void showPatientList() {
        StringBuilder sb = new StringBuilder();
        sb.append("No\tNama Pasien\tNIK\tTanggal Lahir\tAlamat\n");

        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            sb.append(i + 1).append("\t").append(patient.getName()).append("\t")
                    .append(patient.getNik()).append("\t")
                    .append(new SimpleDateFormat("yyyy-MMM-dd").format(patient.getDateOfBirth())).append("\t")
                    .append(patient.getAddress()).append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Daftar Pasien", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateTable() {
        DefaultTableModel tableModel = (DefaultTableModel) tblPatients.getModel();
        tableModel.setRowCount(0);

        for (int i = 0; i < patients.size(); i++) {
            Patient patient = patients.get(i);
            Object[] rowData = {i + 1, patient.getName(), patient.getNik(),
                    new SimpleDateFormat("yyyy-MMM-dd").format(patient.getDateOfBirth()), patient.getAddress()};
            tableModel.addRow(rowData);
        }
    }

    private void clearForm() {
        txtName.setText("");
        txtAddress.setText("");
        txtNik.setText("");
        txtDateOfBirth.setText("");

        // reset currentPatientIndex
        currentPatientIndex = -1;
    }

    private void displayCurrentPatient() {
        Patient patient = patients.get(currentPatientIndex);
        txtName.setText(patient.getName());
        txtAddress.setText(patient.getAddress());
        txtNik.setText(patient.getNik());
        txtDateOfBirth.setText(new SimpleDateFormat("yyyy-MM-dd").format(patient.getDateOfBirth()));
    }

    private boolean isUniqueNik(String nik) {
        for (Patient patient : patients) {
            if (patient.getNik().equals(nik)) {
                return false;
            }
        }
        return true;
    }

    private boolean isUniqueNik(String nik, int currentIndex) {
        for (int i = 0; i < patients.size(); i++) {
            if (i != currentIndex && patients.get(i).getNik().equals(nik)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PatientManagementApp();
            }
        });
    }

}

class Patient {
    private String name;
    private String address;
    private String nik;
    private Date dateOfBirth;

    public Patient(String name, String address, String nik, Date dateOfBirth) {
        this.name = name;
        this.address = address;
        this.nik = nik;
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
