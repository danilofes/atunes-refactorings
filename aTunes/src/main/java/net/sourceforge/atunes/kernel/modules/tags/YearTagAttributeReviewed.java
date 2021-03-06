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

package net.sourceforge.atunes.kernel.modules.tags;

import net.sourceforge.atunes.model.ILocalAudioObject;
import net.sourceforge.atunes.model.ITag;

final class YearTagAttributeReviewed extends AbstractTagAttributeReviewed {

	YearTagAttributeReviewed(final String name) {
		super(name);
	}

	@Override
	String getValue(final ILocalAudioObject audioFile) {
		// we use getTag().getYear() to avoid returning unknown genre
		return Integer.toString(audioFile.getTag().getYear());
	}

	@Override
	ITag changeTag(final ITag tag, final String value) {
		try {
			tag.setYear(Integer.parseInt(value));
		} catch (NumberFormatException e) {
			tag.setYear(0);
		}
		return tag;
	}
}