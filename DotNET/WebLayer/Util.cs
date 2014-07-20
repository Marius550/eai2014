using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Globalization;

namespace WebLayer
{
    public class Util
    {
        public static DateTime ParseDateTime(string dateAsString)
        {
            return DateTime.ParseExact(dateAsString, "dd.MM.yyyy HH:mm", CultureInfo.InvariantCulture);
        }

        public static DateTime ParseDate(string dateAsString)
        {
            return DateTime.ParseExact(dateAsString, "dd.MM.yyyy", CultureInfo.InvariantCulture);
        }
    }
}