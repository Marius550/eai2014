using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Pharmacy.BusinessLayer.Logic
{
    internal static class Util
    {
        internal static void ConvertEmptyToNull(ref String toConvert)
        {
            toConvert = String.IsNullOrEmpty(toConvert) ? null : toConvert;
        }
    }
}
