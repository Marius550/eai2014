using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebLayer
{
    public partial class Errors : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            Exception lastError = Session["LastError"] as Exception;
            Session.Remove("LastError");
            if (lastError != null)
            {
                if (lastError is System.Reflection.TargetInvocationException)
                    lastError = lastError.InnerException;
                ErrorLabel.Text = lastError.Message;
            }
        }
    }
}