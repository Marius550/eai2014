<%@ Page Title="C.Sharpe - Customer Details" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true"
    CodeBehind="Details.aspx.cs" Inherits="WebLayer.Customer.Details" %>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentHolder" runat="server">
    <form id="CustomerDetailsForm" runat="server">
        <asp:ObjectDataSource ID="CustomerDataSource" runat="server"
            SelectMethod="GetCustomer" UpdateMethod="UpdateCustomer"
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
                <asp:TemplateField HeaderText="Telephone number">
                    <ItemTemplate>
                        <asp:Label ID="TelephoneNumber" runat="server" Text='<%# Eval("TelephoneNumber") %>' />
                    </ItemTemplate>
                    <EditItemTemplate>
                        <asp:TextBox ID="TelephoneNumber" runat="server" Text='<%# Bind("TelephoneNumber") %>' />
                        <asp:RequiredFieldValidator ID="TelephoneNumberValidator" runat="server" 
                            ControlToValidate="TelephoneNumber" ErrorMessage="RequiredFieldValidator"
                            EnableClientScript="false">
                            <span class="error">Telephone number required</span>
                        </asp:RequiredFieldValidator>
                   </EditItemTemplate>
                </asp:TemplateField>
                <asp:TemplateField HeaderText="Address">
                    <ItemTemplate>
                        <asp:Label ID="Address" runat="server" Text='<%# Eval("Address") %>' />
                    </ItemTemplate>
                    <EditItemTemplate>
                        <asp:TextBox ID="Address" runat="server" Text='<%# Bind("Address") %>' TextMode="MultiLine" ReadOnly="false" />
                    </EditItemTemplate>
                </asp:TemplateField>
                <asp:CommandField ButtonType="Button" ShowEditButton="true" CancelText="Cancel" EditText="Edit" UpdateText="Update" />
                <asp:HyperLinkField DataNavigateUrlFields="Id"
                    DataNavigateUrlFormatString="~/Prescription/Create.aspx?id={0}"
                    Text="Enter new prescription" />
            </Fields>
            <FooterStyle BackColor="#CCCC99" ForeColor="Black" />
            <HeaderStyle BackColor="#333333" Font-Bold="True" ForeColor="White" />
            <PagerStyle BackColor="White" ForeColor="Black" HorizontalAlign="Right" />
        </asp:DetailsView>
        <h3>Prescriptions</h3>
        <asp:ObjectDataSource ID="PrescriptionDataSource" runat="server"
            SelectMethod="GetAllPrescriptionsForCustomer" TypeName="Pharmacy.BusinessLayer.Logic.PrescriptionService"
            DataObjectTypeName="Pharmacy.BusinessLayer.Data.Prescription">
            <SelectParameters>
                <asp:QueryStringParameter Name="customerId" QueryStringField="id" Type="String" />
            </SelectParameters>
        </asp:ObjectDataSource>
        <asp:GridView ID="PrescriptionsGridView" DataSourceID="PrescriptionDataSource" runat="server" AutoGenerateColumns="false">
            <Columns>
                <asp:BoundField DataField="ID" HeaderText="ID" />
                <asp:BoundField DataField="IssuingPhysician" HeaderText="Issuer" />
                <asp:BoundField DataField="State" HeaderText="State" />
                <asp:HyperLinkField DataNavigateUrlFields="ID" DataNavigateUrlFormatString="~/Prescription/Details.aspx?id={0}" Text="Details" />
            </Columns>
            <EmptyDataTemplate>
                No prescriptions yet.
            </EmptyDataTemplate>
        </asp:GridView>
    </form>
</asp:Content>
