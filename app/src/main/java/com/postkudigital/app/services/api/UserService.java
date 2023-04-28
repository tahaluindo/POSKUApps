package com.postkudigital.app.services.api;

import com.postkudigital.app.json.ActiveStockResponse;
import com.postkudigital.app.json.CalbackPpobResponse;
import com.postkudigital.app.json.CallbackQrisResponse;
import com.postkudigital.app.json.CheckSubsResponse;
import com.postkudigital.app.json.ClaimRequestJson;
import com.postkudigital.app.json.ClaimResponseJson;
import com.postkudigital.app.json.CreateCartRequest;
import com.postkudigital.app.json.CreateCartResponse;
import com.postkudigital.app.json.CreateQrisResponse;
import com.postkudigital.app.json.CreateTokoResponse;
import com.postkudigital.app.json.CustomerPostResponseJson;
import com.postkudigital.app.json.DetailCartResponse;
import com.postkudigital.app.json.DetailMenuResponse;
import com.postkudigital.app.json.DetailStockResponse;
import com.postkudigital.app.json.DetailTransactionResponse;
import com.postkudigital.app.json.GetAbsensiResponse;
import com.postkudigital.app.json.GetArtikelResponse;
import com.postkudigital.app.json.GetBankResponseJson;
import com.postkudigital.app.json.GetCartResponse;
import com.postkudigital.app.json.GetChannelResponseJson;
import com.postkudigital.app.json.GetCheckAbsenResponse;
import com.postkudigital.app.json.GetCustomerResponseJson;
import com.postkudigital.app.json.GetDetailAbseResponse;
import com.postkudigital.app.json.GetDetailArtikelResponse;
import com.postkudigital.app.json.GetDetailBannerResponse;
import com.postkudigital.app.json.GetDetailTransPpobResponse;
import com.postkudigital.app.json.GetHistoryClaimResponse;
import com.postkudigital.app.json.GetHistoryPpobResponse;
import com.postkudigital.app.json.GetHistoryTopupResponse;
import com.postkudigital.app.json.GetHistoryTransResponse;
import com.postkudigital.app.json.GetHistoryWalletResponse;
import com.postkudigital.app.json.GetKategoriResponseJson;
import com.postkudigital.app.json.GetKritikResponse;
import com.postkudigital.app.json.GetMenuResponseJson;
import com.postkudigital.app.json.GetOutletResponseJson;
import com.postkudigital.app.json.GetProdukPponResponse;
import com.postkudigital.app.json.GetPromoResponseJson;
import com.postkudigital.app.json.GetReportResponseJson;
import com.postkudigital.app.json.GetServiceAddResponse;
import com.postkudigital.app.json.GetServiceResponseJson;
import com.postkudigital.app.json.GetTableResponse;
import com.postkudigital.app.json.GetTaxResponseJson;
import com.postkudigital.app.json.GetTipeOrderResponse;
import com.postkudigital.app.json.HomeResponseJson;
import com.postkudigital.app.json.InsertItemResponse;
import com.postkudigital.app.json.KategoriPostResponse;
import com.postkudigital.app.json.KonfirmTopupResponseJson;
import com.postkudigital.app.json.LoginResponseJson;
import com.postkudigital.app.json.PostMenuResponse;
import com.postkudigital.app.json.PpobCategoryResponse;
import com.postkudigital.app.json.PpobProductResponse;
import com.postkudigital.app.json.PromoPostResponseJson;
import com.postkudigital.app.json.RegisterResponseJson;
import com.postkudigital.app.json.ServicePostResponseJson;
import com.postkudigital.app.json.StockResponseJson;
import com.postkudigital.app.json.StockTrxResponse;
import com.postkudigital.app.json.TablePostResponse;
import com.postkudigital.app.json.TaxPostResponseJson;
import com.postkudigital.app.json.TopupResponseJson;
import com.postkudigital.app.json.TransactionResponse;
import com.postkudigital.app.json.TransppobResponseJson;
import com.postkudigital.app.json.TrxStockResponse;
import com.postkudigital.app.json.UpdCartItemRequest;
import com.postkudigital.app.json.UpdateCartRequest;
import com.postkudigital.app.json.WalletResponseJson;
import com.postkudigital.app.models.GetContactUsResponse;

import java.util.Map;

import javax.annotation.Nullable;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface UserService {
//    auth
    @FormUrlEncoded
    @POST("token/register")
    Call<RegisterResponseJson> createUser(@Field("username") String username,
                                          @Field("email") String email,
                                          @Field("password") String password);
    @FormUrlEncoded
    @POST("token/logout")
    Call<GetCheckAbsenResponse> logout(@Field("username") String username);

    @Multipart
    @PUT("updateowner")
    Call<ResponseBody> updateOwner (@Part MultipartBody.Part image,
                                    @PartMap Map<String, RequestBody> text);

    @GET("account")
    Call<ResponseBody> detailAccount(@Query("id_user") String id);

    @Multipart
    @PUT("updatepegawai")
    Call<ResponseBody> updatepegawai (@Part MultipartBody.Part image,
                                    @PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("delete_pegawai")
    Call<ResponseBody> deletePegawai(@PartMap Map<String, RequestBody> text);
//    toko
    @Multipart
    @POST("toko")
    Call<CreateTokoResponse> createToko (@Part MultipartBody.Part image,
                                         @PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("toko")
    Call<CreateTokoResponse> updateToko (@Part MultipartBody.Part image,
                                         @PartMap Map<String, RequestBody> text);

    @GET("toko/detail/{id}")
    Call<CreateTokoResponse> detailToko(@Path("id") String id);

    @Multipart
    @PUT("toko")
    Call<InsertItemResponse> deleteToko(@PartMap Map<String, RequestBody> text);

    @Multipart
    @POST("token/login")
    Call<LoginResponseJson> login (@Part("username") RequestBody username,
                                   @Part("password") RequestBody password);
//    beranda
    @GET("beranda/{id}")
    Call<HomeResponseJson> home(@Path("id") String id);

    @GET("toko")
    Call<GetOutletResponseJson> getToko(@Query("id_owner") String id);

//  menu
    @GET("menu")
    Call<GetMenuResponseJson> getMenu(@Query("id_toko") String id);

    @GET("menu")
    Call<GetMenuResponseJson> getMenuByKategori(@Query("id_toko") String id,
                                                @Nullable @Query("id_kategori") String idcat);

    @Multipart
    @POST("menu")
    Call<PostMenuResponse> addMenu(@Part MultipartBody.Part image,
                                       @PartMap Map<String, RequestBody> text);
    @Multipart
    @PATCH("menu")
    Call<PostMenuResponse> editMenu(@Part MultipartBody.Part image,
                                    @PartMap Map<String, RequestBody> text);

    @GET("menu/detail/{id}")
    Call<DetailMenuResponse> detailMenu(@Path("id") String id);

    @Multipart
    @PUT("menu")
    Call<ResponseBody> deleteMenu(@PartMap Map<String, RequestBody> text);

    @GET("kategorimenu")
    Call<GetKategoriResponseJson> getKategori(@Query("id_toko") String id);

    @Multipart
    @POST("kategorimenu")
    Call<KategoriPostResponse> submitKategori (@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("kategorimenu")
    Call<ResponseBody> updateKategori (@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("kategorimenu")
    Call<ResponseBody> deleteKategori (@PartMap Map<String, RequestBody> text);

    @Multipart
    @POST("stock")
    Call<ActiveStockResponse> activeStock(@PartMap Map<String, RequestBody> text);

    @DELETE("stock")
    Call<KonfirmTopupResponseJson> deleteStock(@Query("menu") String id);

    @GET("stock")
    Call<StockResponseJson> stockList(@Query("id_toko") String id);

    @GET("stock/trx")
    Call<StockTrxResponse> historyStock(@Query("id_menu") String id);

    @GET("stock/detail/{id}")
    Call<DetailStockResponse> detailStock(@Path("id") String id);

    @Multipart
    @POST("stock/trx")
    Call<TrxStockResponse> stocktrx(@PartMap Map<String, RequestBody> text);
//    cart
    @GET("cart")
    Call<GetCartResponse> getCart(@Query("id_toko") String id);

    @GET("cart/detail/{id}")
    Call<DetailCartResponse> detailCart(@Path("id") String id);

    @Multipart
    @POST("cart")
    Call<CreateCartResponse> createCart(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("cart")
    Call<CreateCartResponse> simpanCart(@PartMap Map<String, RequestBody> text);

    @DELETE("cart")
    Call<InsertItemResponse> deleteCart(@Query("id_cart") String id);

    @Multipart
    @POST("cartitem")
    Call<InsertItemResponse> addItem(@PartMap Map<String, RequestBody> text);

    @DELETE("cartitem")
    Call<InsertItemResponse> deleteItem(@Query("id_cart_item") String id);

    @DELETE("cart_item/discount")
    Call<InsertItemResponse> deleteDiskonItem(@Query("id_cart_items") String id);

    @Multipart
    @PATCH("cartitem")
    Call<InsertItemResponse> updateItem(@PartMap Map<String, RequestBody> text);

    @PATCH("v2/cartitem")
    Call<InsertItemResponse> updCartItem(@Body UpdCartItemRequest param);

    @Multipart
    @POST("transaction")
    Call<TransactionResponse> createTransaction(@PartMap Map<String, RequestBody> text);

    @GET("transaction")
    Call<GetHistoryTransResponse> historyTrans(@QueryMap Map<String, String> options);

    @GET("transaction/detail/{code}")
    Call<DetailTransactionResponse> detail(@Path("code") String code);

    @GET("laporanbisnis")
    Call<GetReportResponseJson> laporan(@QueryMap Map<String, String> options);

    @Multipart
    @POST("qris")
    Call<CreateQrisResponse> payQithQris(@PartMap Map<String, RequestBody> text);

    @GET("qris/callback")
    Call<CallbackQrisResponse> callbackQris(@Query("cart_code") String id,
                                            @Query("amount") String date1);

    @GET("qris/check/{invoice}")
    Call<CreateQrisResponse> checkQris(@Path("invoice") String invoice);

    @GET("banner/{id}")
    Call<GetDetailBannerResponse> detailbanner(@Path("id") String id);

    @GET("articles/{id}")
    Call<GetDetailArtikelResponse> detailArtikel(@Path("id") String id);

    @GET("articles")
    Call<GetArtikelResponse> artikel(@Query("page") String page);

    @GET("table")
    Call<GetTableResponse> getTable(@Query("id_toko") String id);

    @Multipart
    @POST("table")
    Call<TablePostResponse> addTable(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("table")
    Call<TablePostResponse> updTable(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("table")
    Call<TablePostResponse> delTable(@PartMap Map<String, RequestBody> text);

    @GET("discount")
    Call<GetPromoResponseJson> getPromo(@Query("id_toko") String id);

    @Multipart
    @POST("discount")
    Call<PromoPostResponseJson> addPromo(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("discount")
    Call<PromoPostResponseJson> updPromo(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("discount")
    Call<PromoPostResponseJson> delPromo(@PartMap Map<String, RequestBody> text);

    @GET("pajak")
    Call<GetTaxResponseJson> getPajak(@Query("id_toko") String id);

    @Multipart
    @POST("pajak")
    Call<TaxPostResponseJson> addPajak(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("pajak")
    Call<TaxPostResponseJson> updPajak(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("pajak")
    Call<TaxPostResponseJson> delPajak(@PartMap Map<String, RequestBody> text);

    @GET("servicefee")
    Call<GetServiceResponseJson> getServiceFee(@Query("id_toko") String id);

    @Multipart
    @POST("servicefee")
    Call<ServicePostResponseJson> addServiceFee(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("servicefee")
    Call<ServicePostResponseJson> updServiceFee(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("servicefee")
    Call<ServicePostResponseJson> delServiceFee(@PartMap Map<String, RequestBody> text);

    @GET("pelanggan")
    Call<GetCustomerResponseJson> getCustomer(@Query("id_toko") String id);

    @Multipart
    @POST("pelanggan")
    Call<CustomerPostResponseJson> addCustomer(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PATCH("pelanggan")
    Call<CustomerPostResponseJson> updCustomer(@PartMap Map<String, RequestBody> text);

    @Multipart
    @PUT("pelanggan")
    Call<CustomerPostResponseJson> delCustomer(@PartMap Map<String, RequestBody> text);

    @FormUrlEncoded
    @POST("wallet")
    Call<WalletResponseJson> activeWallet(@Field("toko") String toko);

    @GET("wallet")
    Call<WalletResponseJson> detailWallet(@Query("toko") String id);

    @GET("channel_payment")
    Call<GetChannelResponseJson> getchannel();

    @FormUrlEncoded
    @POST("wallet/trx")
    Call<TopupResponseJson> topup(@Field("wallet") String wallet,
                                  @Field("adjustment_balance") String balance);

    @Multipart
    @POST("wallet/konfirmasi")
    Call<KonfirmTopupResponseJson> konfirmTopup(@Part MultipartBody.Part image,
                                                @PartMap Map<String, RequestBody> text);

    @GET("wallet/trx")
    Call<GetHistoryWalletResponse> historyWallet(@Query("wallet_id") String id);

    @GET("wallet/history-topup")
    Call<GetHistoryTopupResponse> historyTopup(@Query("wallet_id") String id,
                                               @Query("status_confirm") String status);

    @GET("kategori/ppob")
    Call<PpobCategoryResponse> kategoriPpob();

    @GET("brand/ppob")
    Call<PpobProductResponse> productPpob(@Query("category") String cat);

    @GET("ppob_digi")
    Call<GetProdukPponResponse> ppob(@Query("category") String cat,
                                     @Query("brand") String brand);

    @Multipart
    @POST("ppob_digi")
    Call<TransppobResponseJson> startTrans(@PartMap Map<String, RequestBody> text);

    @GET("ppob_digi/callback")
    Call<CalbackPpobResponse> callback(@Query("ref_id") String refId);


    @GET("transaction/ppob")
    Call<GetHistoryPpobResponse> historyPppob(@QueryMap Map<String, String> options);

    @GET("transaction/ppob/detail")
    Call<GetDetailTransPpobResponse>detailTransPpob(@Query("ref_id") String refId);

    @Multipart
    @PUT("transaction/ppob")
    Call<ResponseBody> refundPpob(@PartMap Map<String, RequestBody> text);

    @GET("settlement")
    Call<GetHistoryTransResponse> settlement(@Query("id_toko") String id);

    @POST("settlement")
    Call<ClaimResponseJson> claim(@Body ClaimRequestJson param);

    @GET("settlement/history")
    Call<GetHistoryClaimResponse> historyClaim(@Query("id_toko") String id,
                                               @Query("status_trx") String status);

    @GET("settlement/{id}")
    Call<ClaimResponseJson> detailSettlement(@Path("id") String id);

    @GET("label_order")
    Call<GetServiceAddResponse> labelorder();

    @GET("tipe_order")
    Call<GetTipeOrderResponse> tipeorder();

    @GET("bank")
    Call<GetBankResponseJson> bank();

    @FormUrlEncoded
    @POST("kritiksaran")
    Call<GetKritikResponse> kritiksaran(@Field("account") String acc,
                                        @Field("label") String label,
                                        @Field("isi") String is);

    @GET("report/menu")
    Call<ResponseBody> reportmenu(@QueryMap Map<String, String> options);

    @GET("report/employee")
    Call<ResponseBody> reportemployee(@QueryMap Map<String, String> options);

    @GET("report/kategori")
    Call<ResponseBody> reportkategori(@QueryMap Map<String, String> options);

    @GET("report/disc")
    Call<ResponseBody> reportdisc(@QueryMap Map<String, String> options);

    @GET("report/table")
    Call<ResponseBody> reporttable(@QueryMap Map<String, String> options);

    @GET("report/pelanggan")
    Call<ResponseBody> reportpelanggan(@QueryMap Map<String, String> options);

    @GET("report/order_tipe")
    Call<ResponseBody> reporttipe(@QueryMap Map<String, String> options);

    @GET("report/label_order")
    Call<ResponseBody> reportlabel(@QueryMap Map<String, String> options);

    @Multipart
    @POST("subs")
    Call<ResponseBody> subs(@PartMap Map<String, RequestBody> text);

    @GET("check_subscribtion")
    Call<CheckSubsResponse> checkSubs(@Query("id_toko") String id);

    @GET("absen")
    Call<GetAbsensiResponse> dataAbsen(@QueryMap Map<String, String> options);

    @GET("absen/check/{id}")
    Call<GetCheckAbsenResponse> checkAbsen(@Path("id") String id);

    @Multipart
    @POST("absen")
    Call<ResponseBody> absen(@Part MultipartBody.Part image,
                             @PartMap Map<String, RequestBody> text);

    @GET("absen/detail/{id}")
    Call<GetDetailAbseResponse> detailAbsen(@Path("id") String id);

    /*cart api v2*/

    @POST("v2/cart")
    Call<CreateCartResponse> createCart(@Body CreateCartRequest param);

    @PATCH("v2/cart")
    Call<CreateCartResponse> updateCart(@Body UpdateCartRequest param);

    @GET("contact_us")
    Call<GetContactUsResponse> contact();

}
