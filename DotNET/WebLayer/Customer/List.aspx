<%@ Page Title="C.Sharpe - Customer List" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="List.aspx.cs" Inherits="WebLayer.Customer.List" %>
<asp:Content ID="Content" ContentPlaceHolderID="ContentHolder" runat="server">
    <form runat="server">
        <asp:EntityDataSource ID="CustomerDataSource" runat="server"
            ConnectionString="name=PharmacyContainer" DefaultContainerName="PharmacyContainer"
            EntitySetName="CustomerSet" EntityTypeFilter="Customer">
        </asp:EntityDataSource>
        <asp:HyperLink NavigateUrl="Create.aspx" Text="Create/add new customer" runat="server" />
        <asp:GridView ID="CustomerGridView" runat="server" DataSourceID="CustomerDataSource" AutoGenerateColumns="false">
            <Columns>
                <asp:BoundField DataField="Name" HeaderText="Name" />
                <asp:BoundField DataField="TelephoneNumber" HeaderText="Telephone Number" />
                <asp:HyperLinkField DataNavigateUrlFields="Id" DataNavigateUrlFormatString="Details.aspx?id={0}"
                    Text="Details" />
            </Columns>
            <EmptyDataTemplate>
                No customers yet.
            </EmptyDataTemplate>
        </asp:GridView>
    </form>
</asp:Content>
