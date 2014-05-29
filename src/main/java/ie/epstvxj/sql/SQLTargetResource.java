/**
 * xAdd Project
 * 
 * @author jun 
 * @since 2 Apr 2014
 */
package ie.epstvxj.sql;

import java.util.List;

/**
 * @author jun
 *
 */
public interface SQLTargetResource extends SQLConstruct {

	public SQLTargetResource withJoin(final SQLJoin join);

	public SQLTargetResource withCorrelationName(final SQLCorrelationName correlationName);

	public SQLCorrelationName getTableCorrelation();

	public String getTableCorrelationName();

	public List<SQLJoin> getJoins();

	@Override
	public SQLTargetResource deepClone();

	@Override
	public SQLTargetResource deepCloneWithPreservedIdentifier();

}
