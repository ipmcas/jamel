package jamel.basic.agents.firms.util;

import jamel.basic.agents.util.LaborPower;
import jamel.basic.util.Commodities;
import jamel.manhattan.InvestmentProcess;

/**
 * Represents the work-in-progress inventory.
 */
public interface WorkInProgress {

	/**
	 * Definitively closes the the process.
	 */
	public abstract void close();

	/**
	 * Returns the ex-post average productivity.
	 * @return the average productivity.
	 */
	public abstract float getAverageProductivity();

	/**
	 * Returns the capacity.
	 * @return the capacity.
	 */
	public abstract float getCapacity();

	/**
	 * Returns the ex-ante average production at full utilization of the production capacity. 
	 * @return the average production.
	 */
	public abstract float getMaxUtilAverageProduction();

	/**
	 * Returns the average productivity.
	 * @return the average productivity.
	 */
	public abstract float getProductivity();

	/**
	 * Returns the total value of the work-in-progress inventory. 
	 * @return the value.
	 */
	public abstract long getValue();

	/**
	 * Returns the value of the unfinished goods at the given stage of the production process.
	 * @param index the stage.
	 * @return the value.
	 */
	public abstract long getValueAt(int index);

	/**
	 * Returns the volume of the unfinished goods at the given stage of the production process.
	 * @param index the stage.
	 * @return the value.
	 */
	public abstract long getVolumeAt(int index);

	/**
	 * Adds the specified investment process. A new machine is created.
	 * @param investmentProcess the investment process to be added.
	 */
	public abstract void investment(InvestmentProcess investmentProcess);

	/**
	 * Produces new goods by the expense of the given labor powers.
	 * @param laborPowers the labor powers.
	 * @return the product.
	 */
	public abstract Commodities process(LaborPower... laborPowers);

	/**
	 * Sets the productivity.
	 * @param productivity the productivity to set.
	 */
	public abstract void setProductivity(float productivity);

}

// ***
