using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace WebLayer.Drug
{
    public partial class List : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void SearchButton_Click(object sender, EventArgs e)
        {
            AllDrugsDatasource.WhereParameters.Clear();

            if (!String.IsNullOrEmpty(SearchBox.Text))
            {
                String pzn = PznOrZero(SearchBox.Text);
                AllDrugsDatasource.Where = "it.[PZN] == @PZN OR Contains(it.[Name], @Name)";
                AllDrugsDatasource.WhereParameters.Add("PZN", TypeCode.Int32, pzn);
                AllDrugsDatasource.WhereParameters.Add("Name", TypeCode.String, SearchBox.Text);
            }
        }

        private string PznOrZero(string pzn)
        {
            int parseResult;
            if (Int32.TryParse(pzn, out parseResult))
            {
                return pzn;
            }
            else
            {
                return "0";
            }

        }

    }
}