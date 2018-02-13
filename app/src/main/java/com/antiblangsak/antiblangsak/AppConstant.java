package com.antiblangsak.antiblangsak;

/**
 * Created by Syukri on 1/1/18.
 */

public class AppConstant {

    public static final int DAFTAR_REKENING_REQUEST_GALLERY = 1;
    public static final int UPLOAD_FOTO_KTP_REQUEST_GALLERY = 2;
    public static final int UPLOAD_FOTO_KK_REQUEST_GALLERY = 3;

    public static final String GENERAL_TEXTVIEW_EMPTY_ERROR_MESSAGE = "Isian harus di isi";
    public static final String GENERAL_CHECKBOX_ERROR_MESSAGE = "Harap tandai setuju";
    public static final String GENERAL_MISSING_FIELD_ERROR_MESSAGE = "Harap lengkapi isian di atas";

    public static final String KEY_IMAGE_BASE64 = "IMAGE_BASE64";

    public static final int HISTORY_PAYMENT_STATUS_WAITING_FOR_PAYMENT = 0;
    public static final int HISTORY_PAYMENT_STATUS_WAITING_FOR_VERIFICATION = 1;
    public static final int HISTORY_PAYMENT_STATUS_ACCEPTED = 2;
    public static final int HISTORY_PAYMENT_STATUS_REJECTED = 3;

    public static final String HISTORY_PAYMENT_STATUS_WAITING_FOR_PAYMENT_STRING = "Menunggu Pembayaran";
    public static final String HISTORY_PAYMENT_STATUS_WAITING_FOR_VERIFICATION_STRING = "Menunggu Verifikasi";
    public static final String HISTORY_PAYMENT_STATUS_ACCEPTED_STRING = "Pembayaran Diterima";
    public static final String HISTORY_PAYMENT_STATUS_REJECTED_STRING = "Pembyaran Ditolak";

    public static final String HISTORY_PAYMENT_STATUS_WAITING_FOR_VERIFICATION_NOTE =
            "Sistem sedang melakukan verifikasi terhadap pembayaran anda. Mohon tunggu beberapa saat.";
}
