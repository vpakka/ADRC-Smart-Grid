/**
 * 
 */
package aDRCsmartgrid.context;

/**
 * @author VijayPakka
 *
 */

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.spi.LoggingEvent;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.space.graph.Network;
import repast.simphony.space.graph.RepastEdge;
import repast.simphony.space.projection.Projection;
import aDRCsmartgrid.agents.Prosumer;
import aDRCsmartgrid.base.Consts;

public class ADRCContext extends DefaultContext{


	public Logger logger;
	protected GregorianCalendar calendar;
	protected int num_prosumers;
	private Network network_Prosumers;
	protected int sp = 0;  					// settlement period
	public int ticksPerDay;
	protected int randomSeed;


	public void setSP (int sp){
		this.sp = sp;
	}

	public int getSP(){
		return this.sp; 
	}

	public void setnum_prosumers (int n){
		this.num_prosumers = n;
	}

	public int getnum_prosumers(){
		return this.num_prosumers;
	}

	protected void setNetworkOfProsumers(Network netProsumers) {
		this.network_Prosumers = netProsumers;
	}

	public Network getNetworkOfProsumers(){
		return this.network_Prosumers;
	}

	protected void setRandomSeedValue(int rs) {
		this.randomSeed = rs;
	}

	public int getRandomSeedValue() {
		return this.randomSeed;
	}

	/**
	 * This method returns the tick time. It is a wrapper around
	 * RepastEssential.GgetTickCount method, which returns the tick count as
	 * integer.
	 * 
	 * @return current tick count of the model
	 */
	public int getTickCount()
	{
		return (int) RepastEssentials.GetTickCount();
	}

	public int getTimeslotOfDay()
	{
		return (int) RepastEssentials.GetTickCount() % this.ticksPerDay;
	}

	/**
	 * This method return the number of <tt> tickPerDay </tt>
	 * 
	 * @return <code>tickPerDay</code>
	 */
	public int getNbOfTickPerDay()
	{
		return this.ticksPerDay;
	}

	public void setNbOfTickPerDay(int tick)
	{
		this.ticksPerDay = tick;

	}

	public ArrayList<Prosumer> getListOfProsumers() {
		ArrayList<Prosumer> aListOfProsumers = new ArrayList<Prosumer>();

		Network adrc_net = getNetworkOfProsumers();
		if (adrc_net != null)
			System.out.println("SUCCESS");
		Iterable<RepastEdge> edgeIter = adrc_net.getEdges();
		if(Consts.VERBOSE) 
			System.out.println("There are "+ adrc_net.size() + " registered Prosumers");
		for (RepastEdge edge : edgeIter) {
			Object obj = edge.getTarget();
			if (obj instanceof Prosumer)
				aListOfProsumers.add((Prosumer) obj);    		
			else
				System.err.println(this.getClass()+"::Wrong Class Type: Prosumer agent is expected");
		}
		return aListOfProsumers;
	}

	/**
	 * This method returns the elapse of time in number of days. It depends on
	 * how a day is initially defined. If a day is divided up to 48 timeslots,
	 * then the second day starts at timeslot 49. However, in order to have it
	 * usefully workable with arrays, the first day is returned as 0, second day
	 * as 1 and so forth.
	 * 
	 * @return the elapsed time in terms of number of day, starting from 0
	 */
	public int getDayCount()
	{
		return (int) RepastEssentials.GetTickCount() / this.getNbOfTickPerDay();
	}

	/**
	 * This method determines whether a day has changed since a given reference
	 * point.
	 * 
	 * @param sinceDay
	 *            a day reference from which the elapse of day is tested.
	 * @return <code>true</code> if the day has changed since <tt>sinceDay</tt>
	 *         <code>false</code> otherwise see {@link #getDayCount()}
	 */
	public boolean isDayChangedSince(int sinceDay)
	{
		boolean dayChanged = false;
		int daysSoFar = this.getDayCount();
		int daysSinceStart = daysSoFar - sinceDay;
		if (daysSinceStart >= 1)
		{
			dayChanged = true;
		}
		return dayChanged;
	}

	/**
	 * This method determines whether a given timeslot is the beginning of the
	 * day It is built rather for readability than its functionality.
	 * 
	 * @param timeslot
	 *            a timeslot of the day to be tested whether it indicates the
	 *            beginning of the day
	 * @return <code>true</code> if given timeslot corresponds to the beginning
	 *         of the day, <code>false</code> otherwise
	 */
	public boolean isBeginningOfDay(int timeslot)
	{
		if (timeslot == 0)
			return true;
		else
			return false;
	}

	/**
	 * This method determines whether a given timeslot is the end of the day It
	 * is built rather for readability than its functionality.
	 * 
	 * @param timeslot
	 *            a timeslot of the day to be tested whether it indicates the
	 *            end of the day
	 * @return <code>true</code> if given timeslot corresponds to the end of the
	 *         day, <code>false</code> otherwise
	 */
	public boolean isEndOfDay(int timeslot)
	{
		if (timeslot == this.ticksPerDay - 1)
			return true;
		else
			return false;
	}

	/**
	 * This method determines whether it is the beginning of the day
	 * 
	 * @return <code>true</code> if it is the beginning of the day,
	 *         <code>false</code> otherwise
	 */
	public boolean isBeginningOfDay()
	{
		double time = RepastEssentials.GetTickCount();
		int timeOfDay = (int) (time % this.getNbOfTickPerDay());
		if (timeOfDay == 0)
			return true;
		else
			return false;
	}

	public GregorianCalendar getCalendar() {
		return this.calendar;
	}

	@ScheduledMethod(start = 0, interval = 1, shuffle = true, priority = ScheduleParameters.FIRST_PRIORITY)
	public void calendarStep()
	{
		this.logger.trace("calenderStep()");  				// problems here. 
		this.calendar.add(Calendar.MINUTE, Consts.MINUTES_PER_DAY / this.ticksPerDay);
	}

	public Date getDateTime()
	{
		return this.calendar.getTime();
	}

	void initDedicatedLogger(String name)
	{
		Level logLevel = Level.toLevel((String) RepastEssentials.GetParameter("loggingLevel"));
		// set up a this.logger for the adoption context
		this.logger = Logger.getLogger(name);
		this.logger.removeAllAppenders();
		this.logger.setLevel(logLevel); // Set this to TRACE for full log files.
		// Can filter what is actually output
		// below
		this.logger.setAdditivity(false);

		ConsoleAppender console = new ConsoleAppender(new ADRCLogLayout());
		console.setName("ConsoleOutput");
		console.setThreshold(Level.INFO);
		console.activateOptions(); // Needed or the appender appends everything
		// from the this.logger

		this.logger.addAppender(console);

		FileAppender traceFile;
		String parsedDate = (new SimpleDateFormat("yyyy.MMM.dd.HH_mm_ss_z")).format(new Date());
		traceFile = new FileAppender();
		traceFile.setLayout(new ADRCLogLayout());
		File logFile = new File("logs", "adrc_" + parsedDate + "_" + logLevel.toString() + ".log");
		traceFile.setFile(logFile.getAbsolutePath());
		// traceFile.setMaxFileSize("1024000");
		// traceFile.setMaxBackupIndex(0);
		traceFile.setName("traceFileOutput");
		traceFile.setThreshold(logLevel);
		traceFile.activateOptions(); // Needed or the appender appends
		// everything from the this.logger

		this.logger.addAppender(traceFile);

		this.logger.info("ADRCsmartgrid Context instantiated and this.logger configured");
		this.logger.info(this.logger);

		this.logger.setAdditivity(false);
	}

	void initPipeToNullLogger()
	{
		this.logger = Logger.getLogger("nullLog");
		this.logger.removeAllAppenders();
		this.logger.setLevel(Level.OFF);
		this.logger.setAdditivity(false);
	}


	public ADRCContext(Context context) {
		super(context.getId(), context.getTypeID());
		this.initDedicatedLogger(Consts.ADRC_LOGGER_NAME);

		this.logger.trace("ADRCContext created with context " + context.getId() + " and type " + context.getTypeID());

		Iterator<Projection<?>> projIterator = context.getProjections().iterator();

		while (projIterator.hasNext()) {
			Projection proj = projIterator.next();
			this.addProjection(proj);
		}

		this.setId(context.getId());
		this.setTypeID(context.getTypeID());

		this.calendar = new GregorianCalendar(0,0,0);
	}

	class ADRCLogLayout extends SimpleLayout
	{
		@Override
		public String format(LoggingEvent ev)
		{
			return "[Tick " + RepastEssentials.GetTickCount() + "; " + (ev.timeStamp - LoggingEvent.getStartTime()) + "] : "
					+ ev.getLevel().toString() + " - " + ev.getRenderedMessage() + "\n";
		}
	}

}

