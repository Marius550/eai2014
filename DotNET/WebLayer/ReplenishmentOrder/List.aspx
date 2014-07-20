<%@ Page Title="C.Sharpe - Replenishment Order List" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" 
        CodeBehind="List.aspx.cs" Inherits="WebLayer.ReplenishmentOrder.List" %>
<asp:Content ID="Content1" ContentPlaceHolderID="ContentHolder" runat="server">
    <form id="ReplenishmentOrderListForm" runat="server">
        <asp:ObjectDataSource ID="AllDOrdersDatasource" runat="server" SelectMethod="GetAllOrders" TypeName="Pharmacy.BusinessLayer.Logic.OrderService"></asp:ObjectDataSource>
        <asp:GridView ID="OrdersGridView" runat="server" AutoGenerateColumns="False" DataSourceID="AllDOrdersDatasource">
            <Columns>
                <asp:BoundField DataField="ID" HeaderText="ID" ReadOnly="True" SortExpression="ID" />
                <asp:BoundField DataField="State" HeaderText="State" SortExpression="State" />
                <asp:BoundField DataField="ExpectedDelivery" HeaderText="Expected Delivery" SortExpression="ExpectedDelivery" DataFormatString="{0:dd.MM.yyyy HH:mm}" />
                <asp:BoundField DataField="ActualDelivery" HeaderText="Actual Delivery" ReadOnly="True" SortExpression="ActualDelivery"  DataFormatString="{0:dd.MM.yyyy HH:mm}" />
                <asp:HyperLinkField DataNavigateUrlFields="ID" DataNavigateUrlFormatString="Details.aspx?id={0}" Text="Details" />
            </Columns>
            <EmptyDataTemplate>
                No replenishment orders yet.
            </EmptyDataTemplate>
        </asp:GridView>
    </form>
</asp:Content>