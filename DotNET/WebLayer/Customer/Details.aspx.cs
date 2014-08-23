using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Net.Mail;

using Pharmacy.BusinessLayer.Logic;

namespace WebLayer.Customer

{
    public partial class Details : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void SendMailBtn_Click(object sender, EventArgs e)
        {

            String receiver = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Email;
            String recipientName = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Name;
            System.Diagnostics.Debug.WriteLine((Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"])).Email));

            MailMessage mailMessage = new MailMessage("pharmacy04@web.de", receiver);
            mailMessage.Subject = "C# Pharmacy04 - Prescription Bill";
            mailMessage.Body = "Dear " + recipientName + ", \n\n Your overall prescription bill is: XXX" + "€." + 
				"\n\n Below you can check your prescription details: "
				+ "\n\n<Id> <Total Price> <Issuer>"
				+ "\n\n" + "XXX"
				+ " \n\n\n Best regards, \n\n C# Pharmacy04";

            SmtpClient smtpClient = new SmtpClient("smtp.web.de", 587); //"smtp.gmail.com"
            smtpClient.Credentials = new System.Net.NetworkCredential()
            {
                UserName = "pharmacy04@web.de",
                Password = "pharmacy04ß?!z"
            };
            smtpClient.EnableSsl = true;
            smtpClient.Send(mailMessage);

        }

        private void Page_Error(object sender, EventArgs e)
        {
            Session["LastError"] = Server.GetLastError();
            Server.ClearError();
            Server.Transfer("~/Error.aspx");
        }
    }
}