<%@ Page Title="C.Sharpe - Create Prescription" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true"
    CodeBehind="Create.aspx.cs" Inherits="WebLayer.Prescription.Create" %>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentHolder" runat="server">
    <form id="CreatePrescriptionForm" runat="server">
        <asp:ObjectDataSource ID="CustomerDataSource" runat="server"
            SelectMethod="GetCustomer"
            TypeName="Pharmacy.BusinessLayer.Logic.CustomerService"
            DataObjectTypeName="Pharmacy.BusinessLayer.Data.Customer">
            <SelectParameters>
                <asp:QueryStringParameter Name="id" QueryStringField="id" Type="Int32" />
            </SelectParameters>
        </asp:ObjectDataSource>
        <asp:DetailsView ID="CustomerDetailsView" runat="server" AutoGenerateRows="false"
            BackColor="White" BorderColor="#CCCCCC" BorderStyle="None" BorderWidth="1px" CellPadding="4"
            ForeColor="Black" GridLines="Horizontal" Height="50px" 
            DataSourceID="CustomerDataSource">
            <EditRowStyle BackColor="#CC3333" Font-Bold="True" ForeColor="White" />
            <Fields>
                <asp:BoundField DataField="Id" HeaderText="Customer Id" ReadOnly="true" />
                <asp:BoundField DataField="Name" HeaderText="Name" ReadOnly="true" />
            </Fields>
            <FooterStyle BackColor="#CCCC99" ForeColor="Black" />
            <HeaderStyle BackColor="#333333" Font-Bold="True" ForeColor="White" />
            <PagerStyle BackColor="White" ForeColor="Black" HorizontalAlign="Right" />
        </asp:DetailsView>
        <p class="label">Issuer</p>
        <p class="value">
            <asp:TextBox ID="IssuerBox" runat="server" />
            <asp:RequiredFieldValidator ID="IssuerBoxValidator" runat="server" 
                ControlToValidate="IssuerBox" ErrorMessage="RequiredFieldValidator"
                EnableClientScript="false">
                <span class="error">Issuer required</span>
            </asp:RequiredFieldValidator>
        </p>
        <p class="form-footer">
            <asp:Button ID="SubmitBtn" runat="server" Text="Create prescription"
                onclick="SubmitBtn_Click" />
        </p>
    </form>
</asp:Content>
