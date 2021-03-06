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

package net.sourceforge.atunes.kernel.modules.search;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.atunes.model.IAudioObject;
import net.sourceforge.atunes.model.IGenre;
import net.sourceforge.atunes.model.IRepositoryHandler;
import net.sourceforge.atunes.utils.I18nUtils;

/**
 * Genre Search field
 * 
 * @author alex
 * 
 */
public class GenreSearchField extends StringSearchField<IGenre> {

	private IRepositoryHandler repositoryHandler;

	/**
	 * @param repositoryHandler
	 */
	public void setRepositoryHandler(final IRepositoryHandler repositoryHandler) {
		this.repositoryHandler = repositoryHandler;
	}

	@Override
	public String getName() {
		return I18nUtils.getString("GENRE");
	}

	@Override
	public List<IAudioObject> getAudioObjects(IGenre genre) {
		return new ArrayList<IAudioObject>(genre.getAudioObjects());
	}

	@Override
	public List<IGenre> getObjectsForEvaluation() {
		return this.repositoryHandler.getGenres();
	}

	@Override
	public String getValueForEvaluation(IGenre genre) {
		return genre.getName();
	}
}
