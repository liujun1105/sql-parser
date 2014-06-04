/**
 * xAdd Project
 * 
 * @author Jun Liu (jun.liu@aol.com) 
 * @since 18 Apr 2014
 */
package ie.epstvxj.sql;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * @author Jun Liu (jun.liu@aol.com)
 *
 */
public class SQLConstructRepository {

	private int											indexer;

	private static Logger								LOG									= Logger.getLogger(SQLConstructRepository.class);

	private final Map<Integer, SQLConstruct>	mapOfRSCIdentifierAndRSCConstruct	= new HashMap<Integer, SQLConstruct>();

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		String header = String.format("%-30s%-30s%-30s", "Index", "Identifier", "SQL");
		stringBuilder.append(header).append("\n");

		for (Entry<Integer, SQLConstruct> entry : mapOfRSCIdentifierAndRSCConstruct.entrySet()) {
			String row = String.format("%-30s%-30s%-30s", entry.getKey(), entry.getValue().getIdentifier(), entry
					.getValue().toSql());
			stringBuilder.append(row).append("\n");
		}
		return stringBuilder.toString();
	}

	public Integer addSQLConstruct(final SQLConstruct rsc) {
		this.mapOfRSCIdentifierAndRSCConstruct.put(++indexer, rsc);
		rsc.withIndex(indexer);
		return indexer;
	}

	public void removeRSC(final int index) {
		this.mapOfRSCIdentifierAndRSCConstruct.remove(index);
	}

	public SQLConstruct getSQLConstruct(final int index) {
		return this.mapOfRSCIdentifierAndRSCConstruct.get(index);
	}

	public void replace(final int index, final SQLConstruct rsc) {
		LOG.trace("replacing [" + this.mapOfRSCIdentifierAndRSCConstruct.get(index) + "] with [" + rsc + "]");
		this.mapOfRSCIdentifierAndRSCConstruct.put(index, rsc);
		rsc.withIndex(index);
	}
}
