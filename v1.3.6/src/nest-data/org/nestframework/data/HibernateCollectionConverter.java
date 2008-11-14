package org.nestframework.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.collection.PersistentCollection;
import org.hibernate.collection.PersistentSet;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

@SuppressWarnings("unchecked")
public class HibernateCollectionConverter implements Converter {

	private Converter listSetConverter;

	private Converter mapConverter;

	private Converter treeMapConverter;

	private Converter treeSetConverter;

	private Converter defaultConverter;

	public HibernateCollectionConverter(ConverterLookup converterLookup) {
		listSetConverter = converterLookup
				.lookupConverterForType(ArrayList.class);
		mapConverter = converterLookup.lookupConverterForType(HashMap.class);
		treeMapConverter = converterLookup
				.lookupConverterForType(TreeMap.class);
		treeSetConverter = converterLookup
				.lookupConverterForType(TreeSet.class);
		defaultConverter = converterLookup.lookupConverterForType(Object.class);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#canConvert(java.lang.Class)
	 */
	public boolean canConvert(Class type) {
		return PersistentCollection.class.isAssignableFrom(type);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#marshal(java.lang.Object,
	 *      com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      com.thoughtworks.xstream.converters.MarshallingContext)
	 */
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		Object collection = source;

		if (source instanceof PersistentCollection) {
			PersistentCollection col = (PersistentCollection) source;
			col.forceInitialization();
			collection = col.getStoredSnapshot();
		}

		// the set is returned as a map by Hibernate (unclear why exactly)
		if (source instanceof PersistentSet) {
			Set set = new HashSet();
			Iterator iter = ((HashMap) collection).entrySet().iterator();
			Map.Entry obj = null;
			while (iter.hasNext()) {
				obj = (Map.Entry) iter.next();
				set.add(obj.getValue());
			}
			collection = set;
//			collection = new HashSet(((HashMap) collection).entrySet());
		}

		// delegate the collection to the approapriate converter
		if (listSetConverter.canConvert(collection.getClass())) {
			listSetConverter.marshal(collection, writer, context);
			return;
		}
		if (mapConverter.canConvert(collection.getClass())) {
			mapConverter.marshal(collection, writer, context);
			return;
		}
		if (treeMapConverter.canConvert(collection.getClass())) {
			treeMapConverter.marshal(collection, writer, context);
			return;
		}
		if (treeSetConverter.canConvert(collection.getClass())) {
			treeSetConverter.marshal(collection, writer, context);
			return;
		}

		defaultConverter.marshal(collection, writer, context);
	}

	/**
	 * @see com.thoughtworks.xstream.converters.Converter#unmarshal(com.thoughtworks.xstream.io.HierarchicalStreamReader,
	 *      com.thoughtworks.xstream.converters.UnmarshallingContext)
	 */
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		return null;
	}

}
