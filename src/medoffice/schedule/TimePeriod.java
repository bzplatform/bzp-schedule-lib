package medoffice.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.DateTime;

public class TimePeriod {

   Date startTime, endTime;

   public TimePeriod() {
   }

   public TimePeriod(Date startTime, Date endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
   }

   public Date getEndTime() {
      return endTime;
   }

   public void setEndTime(Date endTime) {
      this.endTime = endTime;
   }

   public Date getStartTime() {
      return startTime;
   }

   public void setStartTime(Date startTime) {
      this.startTime = startTime;
   }

   public List<Date> timeList(int duration) {
      List<Date> timeList = new ArrayList();
      if (duration > 0) {
         Date time = startTime;
         while ((new DateTime(time).plusMinutes(duration).toDate()).compareTo(endTime) <= 0) {
            timeList.add(time);
            time = new DateTime(time).plusMinutes(duration).toDate();
         }
      }
      return timeList;
   }

   @Override
   public boolean equals(Object object) {
      if (!(object instanceof TimePeriod)) {
         return false;
      }
      TimePeriod other = (TimePeriod) object;
      if (this.startTime == null || this.endTime == null) {
         return false;
      }
      if (this.startTime.equals(other.startTime) && this.endTime.equals(other.endTime)) {
         return true;
      }
      return false;
   }
}
