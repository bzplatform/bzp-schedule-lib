package medoffice.schedule;

import static medoffice.schedule.ScheduleUtil.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatePeriodManager {

   Date startDate, endDate;
   List<DatePeriod> datePeriodList;

   public DatePeriodManager(Date startDate, Date endDate) {
      this.startDate = startDate;
      this.endDate = endDate;

   }

   public List<DatePeriod> getDatePeriodList() {
      if (datePeriodList == null) {
         datePeriodList = new ArrayList();
         Date curDate = startDate;
         while (curDate.compareTo(endDate) <= 0) {
            datePeriodList.add(new DatePeriod(curDate));
            curDate = incDay(curDate);
         }
      }
      return datePeriodList;
   }
   
   public void setDatePeriodList(List<DatePeriod> datePeriodList) {
      this.datePeriodList = datePeriodList;
   }

   public Date getEndDate() {
      return endDate;
   }

   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   public Date getStartDate() {
      return startDate;
   }

   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }
   
}
