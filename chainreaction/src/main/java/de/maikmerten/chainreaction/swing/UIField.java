package de.maikmerten.chainreaction.swing;

import de.maikmerten.chainreaction.Field;
import de.maikmerten.chainreaction.FieldListener;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author maik
 */
public class UIField extends JPanel implements FieldListener, MoveListener {

	private MoveListener moveListener;
	private Field field;
	private List<UICell> cells = new ArrayList<UICell>();

	public UIField(MoveListener listener, Field field) {
		super();
		this.moveListener = listener;

		this.setLayout(new GridLayout(field.getHeight(), field.getWidth()));

		for (int y = 0; y < field.getHeight(); ++y) {
			for (int x = 0; x < field.getWidth(); ++x) {
				UICell cell = new UICell(this, x, y);
				cells.add(cell);
				this.add(cell);
			}
		}
		setField(field);
	}
	
	public final void setField(Field f) {
		this.field = f;
		field.addFieldListener(this);
		onFieldChange(f);
	}
	
	
	public Field getField() {
		return field;
	}
	

	public void onFieldChange(Field f) {
		if (field != f) {
			return;
		}
		for (UICell c : cells) {
			c.updateCell();
		}
	}
	
	public void onMoveSelected(int x, int y) {
		moveListener.onMoveSelected(x, y);
	}


}
