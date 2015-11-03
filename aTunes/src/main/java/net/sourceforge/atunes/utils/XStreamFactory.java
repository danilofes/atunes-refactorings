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

package net.sourceforge.atunes.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;

/**
 * A factory of xstream
 * @author alex
 *
 */
public class XStreamFactory {
	
	private XStream xstream;
	
	private Map<String, String> aliases;
	
	private Map<String, List<String>> omitFields;
	
	/**
	 * Aliases to use when writing XML
	 * @param aliases
	 */
	public void setAliases(Map<String, String> aliases) {
		this.aliases = aliases;
	}
	
	/**
	 * Fields of entities to omit when writing and reading XML
	 * @param omitFields
	 */
	public void setOmitFields(Map<String, List<String>> omitFields) {
		this.omitFields = omitFields;
	}
	
	/**
	 * Returns xstream configuration
	 * @return
	 * @throws ClassNotFoundException
	 */
	public XStream getXStream() throws ClassNotFoundException {
		if (xstream == null) {
			XStream xStream = new XStream();

			for (String alias : aliases.keySet()) {
				xStream.alias(alias, Class.forName(aliases.get(alias)));
			}

			for (String omitClass : omitFields.keySet()) {
				Class<?> clazz = Class.forName(omitClass);
				for (String field : omitFields.get(omitClass)) {
					xStream.omitField(clazz, field);
				}
			}
			xstream = xStream;
        }

        return xstream;
	}

	/**
	 * Reads an object from a file
	 * @param xmlSerializerService TODO
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public Object readObjectFromFile(XMLSerializerService xmlSerializerService, InputStream inputStream) throws IOException {
	    InputStreamReader inputStreamReader = null;
	    try {
	        inputStreamReader = new InputStreamReader(inputStream);
	        return xmlSerializerService.getXStream().fromXML(inputStreamReader);
	    } finally {
	        ClosingUtils.close(inputStreamReader);
	    }
	}

}
