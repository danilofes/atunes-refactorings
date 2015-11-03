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

package net.sourceforge.atunes.kernel.actions;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.KeyStroke;

import net.sourceforge.atunes.model.IAudioObject;
import net.sourceforge.atunes.model.IControlsBuilder;
import net.sourceforge.atunes.model.IPlayListHandler;
import net.sourceforge.atunes.utils.I18nUtils;

/**
 * Switches to next play list or the first one if we are at the last one
 * 
 * @author fleax
 * 
 */
public class NextPlayListAction extends CustomAbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3866441529401824151L;

	private IPlayListHandler playListHandler;

	private IControlsBuilder controlsBuilder;

	/**
	 * @param controlsBuilder
	 */
	public void setControlsBuilder(IControlsBuilder controlsBuilder) {
		this.controlsBuilder = controlsBuilder;
	}

	/**
	 * @param playListHandler
	 */
	public void setPlayListHandler(final IPlayListHandler playListHandler) {
		this.playListHandler = playListHandler;
	}

	/**
	 * Default constructor
	 */
	public NextPlayListAction() {
		super(I18nUtils.getString("NEXT_PLAYLIST"));
	}

	@Override
	protected void initialize() {
		super.initialize();
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(this.controlsBuilder
				.getComponentOrientation().isLeftToRight() ? KeyEvent.VK_RIGHT
				: KeyEvent.VK_LEFT, InputEvent.ALT_MASK));
	}

	@Override
	protected void executeAction() {
		this.playListHandler.nextPlayList();
	}

	@Override
	public boolean isEnabledForPlayListSelection(List<IAudioObject> selection) {
		return this.playListHandler.getPlayListCount() > 1;
	}

}
