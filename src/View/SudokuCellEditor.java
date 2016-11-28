package View;

import java.awt.Font;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;

public class SudokuCellEditor extends DefaultCellEditor {

	private static final long serialVersionUID = 1L;
	private static JTextField field = new JTextField();
	
	public SudokuCellEditor() {
		super(field);
		field.setFont(new Font(Font.MONOSPACED, Font.BOLD, 45));

	}

	
	
}
