package medoffice.schedule;

import java.util.*;
import medoffice.entity.OfficeSchedule;
import medoffice.entity.ProviderSchedule;
import medoffice.entity.ProviderScheduleTime;
import org.joda.time.*;

public class ScheduleUtil {
   
   final static int EX_NO_EXCEPTION = 0, EX_HOLIDAY = 1, EX_MAINTENANCE = 2;
   
   static Date incDay(Date date) {
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      calendar.add(Calendar.DAY_OF_MONTH, 1);
      return calendar.getTime();
   }
   
   static Date incMinute(Date date, int min) {
      Calendar calendar = new GregorianCalendar();
      calendar.setTime(date);
      calendar.add(Calendar.MINUTE, min);
      return calendar.getTime();
   }
   
   static int daySpan(Date date1, Date date2) {
      DateTime dateTime1 = new DateTime(date1);
      DateTime dateTime2 = new DateTime(date2);
      Days d = Days.daysBetween(dateTime1, dateTime2);
      return d.getDays();
   }
   
   static int weekSpan(Date date1, Date date2) {
      DateTime dateTime1 = new DateTime(date1);
      DateTime dateTime2 = new DateTime(date2);
      Weeks w = Weeks.weeksBetween(dateTime1, dateTime2);
      return w.getWeeks();
   }
   
   static int monthSpan(Date date1, Date date2) {
      DateTime dateTime1 = new DateTime(date1);
      DateTime dateTime2 = new DateTime(date2);
      Months m = Months.monthsBetween(dateTime1, dateTime2);
      return m.getMonths();
   }
   
   static int yearSpan(Date date1, Date date2) {
      DateTime dateTime1 = new DateTime(date1);
      DateTime dateTime2 = new DateTime(date2);
      Years y = Years.yearsBetween(dateTime1, dateTime2);
      return y.getYears();
   }
   
   static int nthDayOfWeek(Date date) {
      DateTime dateTime = new DateTime(date);
      int monthOfYear = dateTime.getMonthOfYear();
      int nthDayOfWeek = 0;
      while (dateTime.getMonthOfYear() == monthOfYear) {
         dateTime = dateTime.minusWeeks(1);
         nthDayOfWeek++;
      }
      return nthDayOfWeek;
   }
   
   static boolean isLastWeekDayInMonth(Date date) {
      DateTime dateTime = new DateTime(date);
      if (dateTime.plusWeeks(1).getMonthOfYear() != dateTime.getMonthOfYear()) {
         return true;
      }
      return false;
   }
   
   static boolean isLastWeekInYear(Date date) {
      DateTime dateTime = new DateTime(date);
      if (dateTime.plusWeeks(1).getWeekyear() != dateTime.getWeekyear()) {
         return true;
      }
      return false;
   }
   
   static boolean checkDay(Date curDate, Date startDate, Date endDate, Character period1,
           int everyValue, Character period2, int index2, Character period3, int index3, Character period4, int index4) {
      boolean result = false;
      if (everyValue == 0) {
         result = true;
      }
      if (period1 != null && period1 == 'D') {
         if (everyValue == 1) {
            result = true;
         } else {
            result = (daySpan(startDate, curDate) % everyValue) == 0;
         }
      }
      if (period1 != null && period1 == 'W' && (new DateTime(curDate)).getDayOfWeek() == index2) {
         if (everyValue == 1) {
            result = true;
         } else {
            result = weekSpan(startDate, curDate) % everyValue == 0;
         }
      }
      if (period1 != null && period1 == 'M') {
         if ((everyValue == 1)
                 || (monthSpan(startDate, curDate) % everyValue == 0)) {
            if (period2 != null && period2 == 'D') {
               result = ((new DateTime(curDate)).getDayOfMonth() == index2);
            } else {
               if (period2 != null && period2 == 'W' && ((nthDayOfWeek(curDate) == index2)
                       || (index2 > 5) && isLastWeekDayInMonth(curDate))) {
                  result = ((new DateTime(curDate)).getDayOfWeek() == index3);
               }
            }
         }
      }
      if (period1 != null && period1 == 'Y') {
         if ((everyValue == 1)
                 || (yearSpan(startDate, curDate) % everyValue == 0)) {
            if (period2 != null && period2 == 'D') {
               result = ((new DateTime(curDate)).getDayOfYear() == index2);
            }
            if (period2 != null && period2 == 'W' && (new DateTime(curDate).getWeekOfWeekyear() == index2
                    || (index2 > 53 && isLastWeekInYear(curDate)))) {
               result = ((new DateTime(curDate)).getDayOfWeek() == index3);
            }
            if (period2 != null && period2 == 'M' && (new DateTime(curDate).getMonthOfYear() == index2)
                    || (index2 > 12) && (new DateTime(curDate).getMonthOfYear() == 12)) {
               if (period3 != null && period3 == 'D') {
                  result = ((new DateTime(curDate)).getDayOfMonth() == index3);
               }
               if (period3 != null && period3 == 'W' && ((nthDayOfWeek(curDate) == index3)
                       || ((index3 > 5) && isLastWeekDayInMonth(curDate)))) {
                  result = ((new DateTime(curDate)).getDayOfWeek() == index4);
               }
            }
         }
      }
      result = result && (startDate == null || startDate.compareTo(curDate) <= 0);
      result = result && (endDate == null || curDate.compareTo(endDate) <= 0);
      return result;
   }
   
   public static List<TimeSlot> defineApptSlots(List<AppointmentTime> appointmentList, Date date) {
      List<TimeSlot> busySlotList = new ArrayList();
      TimeSlot timeSlot = null;
      for (AppointmentTime appointmentTime : appointmentList) {
         if (! appointmentTime.getDate().equals(date)) {
            continue;
         }
         if (busySlotList.size() > 0) {
            if (timeSlot.getEndTime().before(appointmentTime.getTime())) {
               timeSlot = new TimeSlot(appointmentTime.getTime(), new DateTime(appointmentTime.getTime()).plusMinutes(appointmentTime.getDuration()).toDate());
               busySlotList.add(timeSlot);
            } else {
               if (timeSlot.getEndTime().compareTo(appointmentTime.getTime()) == 0
                       || timeSlot.getStartTime().before(appointmentTime.getTime())
                       && timeSlot.getEndTime().before(new DateTime(appointmentTime.getTime()).plusMinutes(appointmentTime.getDuration()).toDate())) {
                  timeSlot.setEndTime(new DateTime(appointmentTime.getTime()).plusMinutes(appointmentTime.getDuration()).toDate());
               }
            }
         } else {
            timeSlot = new TimeSlot(appointmentTime.getTime(), new DateTime(appointmentTime.getTime()).plusMinutes(appointmentTime.getDuration()).toDate());
            busySlotList.add(timeSlot);
         }
      }
      return busySlotList;
   }
   
   public static void defineAvailableDatePeriods(List<AppointmentTime> appointmentTimeList, List<TimePeriodManager> timePeriodManagerList, List<ProviderSchedule> providerScheduleList,
           List<OfficeSchedule> officeScheduleList, Date startDate, Date endDate, int categoryId) {
      List<TimeSlot> busySlotList;
      DatePeriodManager datePeriodManager = new DatePeriodManager(startDate, endDate);
      for (ProviderSchedule providerSchedule : providerScheduleList) {
         for (DatePeriod datePeriod : datePeriodManager.getDatePeriodList()) {
            if (checkDay(datePeriod.getCurDate(), providerSchedule.getStartDate(),
                    providerSchedule.getEndDate(),
                    providerSchedule.getPeriod1Code(), providerSchedule.getPeriod1EveryValue(),
                    providerSchedule.getPeriod2Code(), providerSchedule.getPeriod2Index(),
                    providerSchedule.getPeriod3Code(), providerSchedule.getPeriod3Index(),
                    providerSchedule.getPeriod4Code(), providerSchedule.getPeriod4Index())) {
               if (datePeriod.getProviderSchedule() == null) {
                  datePeriod.setProviderSchedule(providerSchedule);
                  datePeriod.setEveryValue(providerSchedule.getPeriod1EveryValue());
                  datePeriod.setClassifierCode(providerSchedule.getClassifierCode());
               } else {
                  if (datePeriod.getEveryValue() > 0 && providerSchedule.getPeriod1EveryValue() == 0) {
                     datePeriod.setProviderSchedule(providerSchedule);
                     datePeriod.setEveryValue(providerSchedule.getPeriod1EveryValue());
                     datePeriod.setClassifierCode(providerSchedule.getClassifierCode());
                  } else if (datePeriod.getClassifierCode() == 'S' && providerSchedule.getClassifierCode() != 'S') {
                     datePeriod.setProviderSchedule(providerSchedule);
                     datePeriod.setEveryValue(providerSchedule.getPeriod1EveryValue());
                     datePeriod.setClassifierCode(providerSchedule.getClassifierCode());
                  }
               }
            }
         }
      }
      for (OfficeSchedule officeSchedule : officeScheduleList) {
         for (DatePeriod datePeriod : datePeriodManager.getDatePeriodList()) {
            if (checkDay(datePeriod.getCurDate(), officeSchedule.getStartDate(),
                    officeSchedule.getEndDate(),
                    officeSchedule.getPeriod1Code(), officeSchedule.getPeriod1EveryValue(),
                    officeSchedule.getPeriod2Code(), officeSchedule.getPeriod2Index(),
                    officeSchedule.getPeriod3Code(), officeSchedule.getPeriod3Index(),
                    officeSchedule.getPeriod4Code(), officeSchedule.getPeriod4Index())) {
               if (datePeriod.getProviderSchedule() != null && datePeriod.getEveryValue() > 0) {
                  if (datePeriod.getProviderSchedule().getExceptOfficeHolidays() && officeSchedule.getClassifierCode() == 'H') {
                     datePeriod.setProviderSchedule(null);
                     datePeriod.setEveryValue(-1);
                     datePeriod.setClassifierCode(officeSchedule.getClassifierCode());                     
                  }
               }
            }
         }         
      }
      for (int i = 0; i < datePeriodManager.getDatePeriodList().size(); i++) {
         TimePeriodManager timePeriodManager = timePeriodManagerList.get(i);
         DatePeriod datePeriod = datePeriodManager.getDatePeriodList().get(i);
         timePeriodManager.setActiveDate(datePeriod.getCurDate());
         timePeriodManager.setClassifierCode(datePeriod.getClassifierCode());
         if (timePeriodManager.active() && datePeriod.getProviderSchedule() != null) {
            for (ProviderScheduleTime scheduleTime : datePeriod.getProviderSchedule().getProviderScheduleTimeList()) {
               TimePeriod timePeriod = new TimePeriod();
               timePeriod.setStartTime(scheduleTime.getStartTime());
               timePeriod.setEndTime(scheduleTime.getEndTime());
               timePeriodManager.getTimePeriodList().add(timePeriod);
            }
            for (ProviderScheduleTime scheduleTime : datePeriod.getProviderSchedule().getProviderScheduleTimeList()) {
               TimePeriod timePeriod = new TimePeriod();
               timePeriod.setStartTime(scheduleTime.getStartTime());
               timePeriod.setEndTime(scheduleTime.getEndTime());
               timePeriodManager.getScheduleTimePeriodList().add(timePeriod);
            }
            busySlotList = defineApptSlots(appointmentTimeList, timePeriodManager.getActiveDate());
            timePeriodManager.schrinkSlotsByBusyTime(busySlotList);
            timePeriodManager.defineAvailablePeriods();
         }
      }
      
      
   }
}
