package View;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class SudokuCellRender extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        if ((column % 3) == 0)
            left = 2;
        
        else if ((column % 3) == 2)
            right = 2;

        if ((row % 3) == 0)
            top = 2;
        
        if ((row % 3) == 2)
            bottom = 2;

        setBorder(new MatteBorder(top, left, bottom, right, Color.BLACK));
        setHorizontalAlignment(0);
        
        if(hasFocus){
        	setBorder(new MatteBorder(2, 2, 2, 2, Color.RED));
        }

        return this;
    }

}
