/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * Copyright 2012 Arcusys Oy, Finland. All rights reserved.
 *
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holders (http://www.ixonos.com/, http://www.arcusys.fi/).
 * 
 */
package fi.koku.calendar;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * @author Dmitry Kudinov (dmitry.kudinov@arcusys.fi)
 * @author laukksa / Ixonos
 * 
 * Jul 25, 2011
 */
public class CalendarUtil {

  /**
   * Convert to XMLGregorianCalendar with date and time.
   * 
   * @param date
   * @return XMLGregorianCalendar
   */
  public static XMLGregorianCalendar getXmlDateTime(final Date date) {
    return getXmlCalendar(date, new SetCalendar());
  }

  /**
   * Convert to XMLGregorianCalendar with date only (time set to zero).
   * 
   * @param date
   * @return XMLGregorianCalendar
   */
  public static XMLGregorianCalendar getXmlDate(final Date date) {
    return getXmlCalendar(date, new SetDateOnlyCalendar());
  }

  public static XMLGregorianCalendar getXmlTime(final Date date, final int minutes) {
    return getXmlCalendar(date, new SetDateOnlyCalendar() {
      @Override
      void setCalendar(Calendar calendar) {
        super.setCalendar(calendar);
        calendar.set(Calendar.MINUTE, minutes);
      }
    });
  }

  private static XMLGregorianCalendar getXmlCalendar(final Date date, final SetCalendar setCalendar) {
    if (date == null) {
      return null;
    }
    try {
      final GregorianCalendar calendar = (GregorianCalendar)GregorianCalendar.getInstance();
      calendar.setTime(date);
      setCalendar.setCalendar(calendar);
      return DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
    } catch (DatatypeConfigurationException e) {
      throw new RuntimeException(e);
    }
  }
  
  private static class SetCalendar {
    void setCalendar(final Calendar calendar){
    }
  }

  private static class SetDateOnlyCalendar extends SetCalendar {
    void setCalendar(final Calendar calendar){
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
    }
  }

    /**
     * @param createdFromDate
     * @return
     */
    public static Date getDate(XMLGregorianCalendar calendar) {
        if (calendar == null) {
            return null;
        }
        return calendar.toGregorianCalendar().getTime();
    }
}

