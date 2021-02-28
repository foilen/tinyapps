/*
    Tinyapps
    https://github.com/provirus/tinyapps
    Copyright (c) 2014-2021 Foilen (https://foilen.com)

    The MIT License
    http://opensource.org/licenses/MIT

 */
package com.foilen.gnucashbulk.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.foilen.gnucashbulk.action.MergeAllPreviousYearsAction;
import com.foilen.gnucashbulk.domain.Account;

public class AccountChooser extends JFrame {

    private static final long serialVersionUID = 1L;

    private MergeAllPreviousYearsAction mergeAllPreviousYearsAction;

    private JPanel contentPane;
    private JTable accountTable;

    public AccountChooser(final MergeAllPreviousYearsAction mergeAllPreviousYearsAction, List<Account> accounts) {

        this.mergeAllPreviousYearsAction = mergeAllPreviousYearsAction;

        setTitle("GnuCash Bulk - Choose balance account");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 482, 506);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        final JButton btnSelectThisAccount = new JButton("Select this account as the Opening Balance account");
        btnSelectThisAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = accountTable.getSelectedRow();
                mergeAllPreviousYearsAction.setBalanceAccount(mergeAllPreviousYearsAction.getSelectableAccounts().get(selectedRow));
                mergeAllPreviousYearsAction.lastSteps();
                dispose();
            }
        });
        btnSelectThisAccount.setEnabled(false);

        accountTable = new JTable();
        accountTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnSelectThisAccount.setEnabled(accountTable.getSelectedRow() != -1);
            }
        });
        contentPane.add(new JScrollPane(accountTable));

        contentPane.add(btnSelectThisAccount);

        refreshAll();
    }

    private Object[][] convertToData(List<Object[]> entries) {
        return entries.toArray(new Object[1][]);
    }

    private void refreshAll() {

        // Show the accounts
        List<Object[]> entries = new ArrayList<>(mergeAllPreviousYearsAction.getSelectableAccounts().size());
        for (Account account : mergeAllPreviousYearsAction.getSelectableAccounts()) {
            entries.add(new String[] { account.getGuid(), account.getName() });
        }
        accountTable.setModel(new DefaultTableModel(convertToData(entries), new String[] { "Account GUID", "Account Name" }));

    }

}
