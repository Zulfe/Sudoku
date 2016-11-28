package View;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class SudokuTable extends JTable {
	
	public SudokuTable(){
		super(new DefaultTableModel(9, 9));
		setRowSelectionAllowed(false);
		setDefaultRenderer(Object.class, new SudokuCellRender());
		setDefaultEditor(Object.class, new SudokuCellEditor());
	}
}
