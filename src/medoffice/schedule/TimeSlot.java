package medoffice.schedule;

import java.util.Date;

public class TimeSlot {
    Date startTime, endTime;

   public TimeSlot(Date startTime, Date endTime) {
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
}
