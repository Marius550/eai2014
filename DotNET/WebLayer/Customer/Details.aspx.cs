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
        
        protected void Update_Command(object sender, EventArgs e)
        {
            //Text='<%# String.Format("{0:dd.MM.yyyy}", DateTime.Now) %>'

            TextBox birthDateString = CustomerDetailsView.FindControl("BirthDateBox") as TextBox;
            DateTime birthDate = Util.ParseDate(birthDateString.Text);

            TextBox telephoneNumberString = CustomerDetailsView.FindControl("TelephoneNumber") as TextBox;
            String telephoneNumber = telephoneNumberString.Text;

            TextBox addressString = CustomerDetailsView.FindControl("Address") as TextBox;
            String address = addressString.Text;

            TextBox emailString = CustomerDetailsView.FindControl("Email") as TextBox;
            String email = emailString.Text;

            Pharmacy.BusinessLayer.Data.Customer customer = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(Convert.ToInt32(Request.QueryString["id"]));
            Pharmacy.BusinessLayer.Logic.CustomerService.UpdateCustomer(customer, telephoneNumber, address, email, birthDate);
        }

        public string buildCustomerEmailinformationString(int prescriptionIndex, Int32 customerId)
        {
            ICollection<Pharmacy.BusinessLayer.Data.Prescription> customerPrescriptions = new List<Pharmacy.BusinessLayer.Data.Prescription>();
            customerPrescriptions = Pharmacy.BusinessLayer.Logic.PrescriptionService.GetAllPrescriptionsForCustomer(customerId);

            List<string> prescriptionIdList = new List<string>();
            List<string> prescriptionIssuingPhysicianList = new List<string>();
            List<string> prescriptionTotalPriceList = new List<string>();
            foreach (Pharmacy.BusinessLayer.Data.Prescription i in customerPrescriptions)
            {
                prescriptionIdList.Add(Convert.ToString((Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(i.Id).Id)));
                prescriptionIssuingPhysicianList.Add(Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(i.Id).IssuingPhysician);
                prescriptionTotalPriceList.Add(Convert.ToString(Pharmacy.BusinessLayer.Logic.PrescriptionService.GetPrescription(i.Id).TotalPrice));
            }
            return prescriptionIdList[prescriptionIndex] + " " + prescriptionIssuingPhysicianList[prescriptionIndex] + " " + prescriptionTotalPriceList[prescriptionIndex];
        }

        protected string buildCustomerEmailInformationStringHandler()
        {
            Int32 customerId = Convert.ToInt32(Request.QueryString["id"]);
            int customerPrescriptionCollectionLength = Pharmacy.BusinessLayer.Logic.PrescriptionService.GetAllPrescriptionsForCustomer(customerId).Count;

            string customerEmailinformationOutputString = null;

            if (customerPrescriptionCollectionLength != 0)
            {
                for (int prescriptionIndex = 0; prescriptionIndex < customerPrescriptionCollectionLength; prescriptionIndex++)
                {
                    customerEmailinformationOutputString = customerEmailinformationOutputString + buildCustomerEmailinformationString(prescriptionIndex, customerId) + "\n";
                }
            }
            else
            {
                customerEmailinformationOutputString = "No prescription submitted yet.";
            }
            return customerEmailinformationOutputString;
        }

    protected void SendMailBtnException_Click(object sender, EventArgs e) {

            Int32 customerId = Convert.ToInt32(Request.QueryString["id"]);

            String recipientName = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(customerId).Name;
            String recipientEmail = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(customerId).Email;
            double prescriptionBill = Pharmacy.BusinessLayer.Logic.CustomerService.GetCustomer(customerId).PrescriptionBill;

            MailMessage mailMessage = new MailMessage("pharmacy04@web.de", recipientEmail);
                mailMessage.Subject = "C# Pharmacy04 - Prescription Bill";
                mailMessage.Body = "Dear " + recipientName + ", \n\n Your overall prescription bill is: " + prescriptionBill + "€." +
                    "\n\n Below you can check your prescription details: "
                    + "\n\n<Id> <Issuer> <Total Price>"
                    + "\n\n" + buildCustomerEmailInformationStringHandler()
                    + " \n\n\n Best regards, \n\n C# Pharmacy04";

                SmtpClient smtpClient = new SmtpClient("smtp.web.de", 587);
                smtpClient.Credentials = new System.Net.NetworkCredential()
                {
                    UserName = "pharmacy04@web.de",
                    Password = "pharmacy04ß?!z"
                };
                smtpClient.EnableSsl = true;
        try
			{
                ResultLabel.Text = null;
                smtpClient.Send(mailMessage);
                ResultLabel.Text = String.Format("Sending email to " + recipientEmail + " succeeded");
                ResultLabel.CssClass = "success";
			}
			catch (SmtpFailedRecipientsException ex)
			{
				for (int i = 0; i < ex.InnerExceptions.Length; i++)
				{
					SmtpStatusCode status = ex.InnerExceptions[i].StatusCode;
					if (status == SmtpStatusCode.MailboxBusy ||
						status == SmtpStatusCode.MailboxUnavailable)
					{
                        ResultLabel.Text = String.Format("Delivery failed - retrying in 5 seconds.");
                        ResultLabel.CssClass = "error";

						System.Threading.Thread.Sleep(5000);
                        smtpClient.Send(mailMessage);
					}
					else
					{
                        ResultLabel.Text = String.Format("Failed to deliver message to " + recipientEmail);
                        ResultLabel.CssClass = "error";
					}
				}
			}
            catch (Exception ex)
            {
                ResultLabel.Text = String.Format("Exception caught in RetryIfBusy(): " + ex);
                ResultLabel.CssClass = "error";
            }
        }

        private void Page_Error(object sender, EventArgs e)
        {
            Session["LastError"] = Server.GetLastError();
            Server.ClearError();
            Server.Transfer("~/Error.aspx");
        }
    }
}
