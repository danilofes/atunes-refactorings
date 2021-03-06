/*
 * aTunes
 * Copyright (C) Alex Aranda, Sylvain Gaudard and contributors
 *
 * See http://www.atunes.org/wiki/index.php?title=Contributing for information about contributors
 *
 * http://www.atunes.org
 * http://sourceforge.net/projects/atunes
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package net.sourceforge.atunes.gui.autocomplete;

import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ComboBoxEditor;
import javax.swing.InputMap;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import org.jdesktop.swingx.autocomplete.AbstractAutoCompleteAdaptor;
import org.jdesktop.swingx.autocomplete.AutoCompleteComboBoxEditor;
import org.jdesktop.swingx.autocomplete.ComboBoxAdaptor;
import org.jdesktop.swingx.autocomplete.ListAdaptor;
import org.jdesktop.swingx.autocomplete.ObjectToStringConverter;
import org.jdesktop.swingx.autocomplete.TextComponentAdaptor;

/**
 * This class contains only static utility methods that can be used to set up
 * automatic completion for some Swing components.
 * <p>
 * Usage examples:
 * </p>
 * <p>
 * 
 * <pre>
 * &lt;code&gt;
 * JComboBox comboBox = [...];
 * AutoCompleteDecorator.&lt;b&gt;decorate&lt;/b&gt;(comboBox);
 * 
 * List items = [...];
 * JTextField textField = [...];
 * AutoCompleteDecorator.&lt;b&gt;decorate&lt;/b&gt;(textField, items);
 * 
 * JList list = [...];
 * JTextField textField = [...];
 * AutoCompleteDecorator.&lt;b&gt;decorate&lt;/b&gt;(list, textField);
 * &lt;/code&gt;
 * </pre>
 * 
 * </p>
 * 
 * @author Thomas Bierhance
 */
public final class AutoCompleteDecorator {
    /**
     * private constructor to avoid instantiating utility class
     */
    private AutoCompleteDecorator() {
    }

    /**
     * Enables automatic completion for the given JTextComponent based on the
     * items contained in the given <tt>List</tt>.
     * 
     * @param textComponent
     *            the text component that will be used for automatic completion.
     * @param items
     *            contains the items that are used for autocompletion
     * @param strictMatching
     *            <tt>true</tt>, if only given items should be allowed to be
     *            entered
     */
    public static void decorate(final JTextComponent textComponent,
	    final List<Object> items, final boolean strictMatching) {
	decorate(textComponent, items, strictMatching,
		ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
    }

    /**
     * Enables automatic completion for the given JTextComponent based on the
     * items contained in the given <tt>List</tt>.
     * 
     * @param items
     *            contains the items that are used for autocompletion
     * @param textComponent
     *            the text component that will be used for automatic completion.
     * @param strictMatching
     *            <tt>true</tt>, if only given items should be allowed to be
     *            entered
     * @param stringConverter
     *            the converter used to transform items to strings
     */
    public static void decorate(final JTextComponent textComponent,
	    final List<Object> items, final boolean strictMatching,
	    final ObjectToStringConverter stringConverter) {
	AbstractAutoCompleteAdaptor adaptor = new TextComponentAdaptor(
		textComponent, items);
	AutoCompleteDocument document = new AutoCompleteDocument(adaptor,
		strictMatching, stringConverter);
	decorate(textComponent, document, adaptor);
    }

    /**
     * Enables automatic completion for the given JTextComponent based on the
     * items contained in the given JList. The two components will be
     * synchronized. The automatic completion will always be strict.
     * 
     * @param list
     *            a <tt>JList</tt> containing the items for automatic completion
     * @param textComponent
     *            the text component that will be enabled for automatic
     *            completion
     */
    public static void decorate(final JList list,
	    final JTextComponent textComponent) {
	decorate(list, textComponent,
		ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
    }

    /**
     * Enables automatic completion for the given JTextComponent based on the
     * items contained in the given JList. The two components will be
     * synchronized. The automatic completion will always be strict.
     * 
     * @param list
     *            a <tt>JList</tt> containing the items for automatic completion
     * @param textComponent
     *            the text component that will be used for automatic completion
     * @param stringConverter
     *            the converter used to transform items to strings
     */
    public static void decorate(final JList list,
	    final JTextComponent textComponent,
	    final ObjectToStringConverter stringConverter) {
	AbstractAutoCompleteAdaptor adaptor = new ListAdaptor(list,
		textComponent, stringConverter);
	AutoCompleteDocument document = new AutoCompleteDocument(adaptor, true,
		stringConverter);
	decorate(textComponent, document, adaptor);
    }

    /**
     * Enables automatic completion for the given JComboBox. The automatic
     * completion will be strict (only items from the combo box can be selected)
     * if the combo box is not editable.
     * 
     * @param comboBox
     *            a combo box
     */
    public static void decorate(final JComboBox comboBox) {
	decorate(comboBox, ObjectToStringConverter.DEFAULT_IMPLEMENTATION);
    }

    /**
     * Enables automatic completion for the given JComboBox. The automatic
     * completion will be strict (only items from the combo box can be selected)
     * if the combo box is not editable.
     * 
     * @param comboBox
     *            a combo box
     * @param stringConverter
     *            the converter used to transform items to strings
     */
    public static void decorate(final JComboBox comboBox,
	    final ObjectToStringConverter stringConverter) {
	boolean strictMatching = !comboBox.isEditable();
	// has to be editable
	comboBox.setEditable(true);
	// configure the text component=editor component
	JTextComponent editorComponent = (JTextComponent) comboBox.getEditor()
		.getEditorComponent();
	final AbstractAutoCompleteAdaptor adaptor = new ComboBoxAdaptor(
		comboBox);
	final AutoCompleteDocument document = new AutoCompleteDocument(adaptor,
		strictMatching, stringConverter);
	decorate(editorComponent, document, adaptor);

	// show the popup list when the user presses a key
	final KeyListener keyListener = new PopUpListKeyAdapter(comboBox);
	editorComponent.addKeyListener(keyListener);

	if (stringConverter != ObjectToStringConverter.DEFAULT_IMPLEMENTATION) {
	    comboBox.setEditor(new AutoCompleteComboBoxEditor(comboBox
		    .getEditor(), stringConverter));
	}

	// Changing the l&f can change the combobox' editor which in turn
	// would not be autocompletion-enabled. The new editor needs to be
	// set-up.
	comboBox.addPropertyChangeListener("editor",
		new PropertyChangeListener() {
		    @Override
		    public void propertyChange(final PropertyChangeEvent e) {
			ComboBoxEditor editor = (ComboBoxEditor) e
				.getNewValue();
			if (editor != null
				&& editor.getEditorComponent() != null) {
			    if (!(editor instanceof AutoCompleteComboBoxEditor)
				    && stringConverter != ObjectToStringConverter.DEFAULT_IMPLEMENTATION) {
				comboBox.setEditor(new AutoCompleteComboBoxEditor(
					editor, stringConverter));
				// Don't do the decorate step here because
				// calling
				// setEditor will trigger
				// the propertychange listener a second time,
				// which will
				// do the decorate
				// and addKeyListener step.
			    } else {
				decorate((JTextComponent) editor
					.getEditorComponent(), document,
					adaptor);
				editor.getEditorComponent().addKeyListener(
					keyListener);
			    }
			}
		    }
		});
    }

    /**
     * Decorates a given text component for automatic completion using the given
     * AutoCompleteDocument and AbstractAutoCompleteAdaptor.
     * 
     * 
     * @param textComponent
     *            a text component that should be decorated
     * @param document
     *            the AutoCompleteDocument to be installed on the text component
     * @param adaptor
     *            the AbstractAutoCompleteAdaptor to be used
     */
    public static void decorate(final JTextComponent textComponent,
	    final AutoCompleteDocument document,
	    final AbstractAutoCompleteAdaptor adaptor) {
	// install the document on the text component
	textComponent.setDocument(document);

	// mark entire text when the text component gains focus
	// otherwise the last mark would have been retained which is quiet
	// confusing
	textComponent.addFocusListener(new FocusAdapter() {
	    @Override
	    public void focusGained(final FocusEvent e) {
		adaptor.markEntireText();
	    }
	});

	// Tweak some key bindings
	InputMap editorInputMap = textComponent.getInputMap();
	if (document.isStrictMatching()) {
	    // move the selection to the left on VK_BACK_SPACE
	    editorInputMap.put(KeyStroke.getKeyStroke(
		    java.awt.event.KeyEvent.VK_BACK_SPACE, 0),
		    DefaultEditorKit.selectionBackwardAction);
	    // ignore VK_DELETE and CTRL+VK_X and beep instead when strict
	    // matching
	    editorInputMap.put(KeyStroke.getKeyStroke(
		    java.awt.event.KeyEvent.VK_DELETE, 0), errorFeedbackAction);
	    editorInputMap.put(KeyStroke.getKeyStroke(
		    java.awt.event.KeyEvent.VK_X,
		    java.awt.event.InputEvent.CTRL_DOWN_MASK),
		    errorFeedbackAction);
	} else {
	    ActionMap editorActionMap = textComponent.getActionMap();
	    // leave VK_DELETE and CTRL+VK_X as is
	    // VK_BACKSPACE will move the selection to the left if the selected
	    // item is in the list
	    // it will delete the previous character otherwise
	    editorInputMap.put(KeyStroke.getKeyStroke(
		    java.awt.event.KeyEvent.VK_BACK_SPACE, 0),
		    "nonstrict-backspace");
	    editorActionMap
		    .put("nonstrict-backspace",
			    new NonStrictBackspaceAction(
				    editorActionMap
					    .get(DefaultEditorKit.deletePrevCharAction),
				    editorActionMap
					    .get(DefaultEditorKit.selectionBackwardAction),
				    adaptor));
	}
    }

    private static final class PopUpListKeyAdapter extends KeyAdapter {
	private final JComboBox comboBox;

	private PopUpListKeyAdapter(final JComboBox comboBox) {
	    this.comboBox = comboBox;
	}

	@Override
	public void keyPressed(final KeyEvent keyEvent) {
	    // don't popup on action keys (cursor movements, etc...)
	    if (keyEvent.isActionKey()) {
		return;
	    }

	    // don't popup if the combobox isn't visible anyway
	    if (comboBox.isDisplayable() && !comboBox.isPopupVisible()) {
		int keyCode = keyEvent.getKeyCode();
		// don't popup when the user hits shift,ctrl or alt
		if (keyCode == KeyEvent.VK_SHIFT
			|| keyCode == KeyEvent.VK_CONTROL
			|| keyCode == KeyEvent.VK_ALT) {
		    return;
		}
		// don't popup when the user hits escape (see issue #311)
		if (keyCode == KeyEvent.VK_ESCAPE) {
		    return;
		}

		comboBox.setPopupVisible(true);
	    }
	}
    }

    static class NonStrictBackspaceAction extends TextAction {
	private static final long serialVersionUID = -5508607690462561673L;

	private final Action backspace;

	private final Action selectionBackward;

	private final AbstractAutoCompleteAdaptor adaptor;

	/**
	 * @param backspace
	 * @param selectionBackward
	 * @param adaptor
	 */
	public NonStrictBackspaceAction(final Action backspace,
		final Action selectionBackward,
		final AbstractAutoCompleteAdaptor adaptor) {
	    super("nonstrict-backspace");
	    this.backspace = backspace;
	    this.selectionBackward = selectionBackward;
	    this.adaptor = adaptor;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
	    if (adaptor.listContainsSelectedItem()) {
		selectionBackward.actionPerformed(e);
	    } else {
		backspace.actionPerformed(e);
	    }
	}
    }

    /**
     * A TextAction that provides an error feedback for the text component that
     * invoked the action. The error feedback is most likely a "beep".
     */
    private static Object errorFeedbackAction = new TextAction(
	    "provide-error-feedback") {
	private static final long serialVersionUID = -3868819565696640330L;

	@Override
	public void actionPerformed(final ActionEvent e) {
	    UIManager.getLookAndFeel()
		    .provideErrorFeedback(getTextComponent(e));
	}
    };
}
