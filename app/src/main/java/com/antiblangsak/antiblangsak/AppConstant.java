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
    public static final String HISTORY_PAYMENT_STATUS_ACCEPTED_STRING = "Diterima";
    public static final String HISTORY_PAYMENT_STATUS_REJECTED_STRING = "Ditolak";

    public static final String HISTORY_PAYMENT_STATUS_WAITING_FOR_VERIFICATION_NOTE =
            "Sistem sedang melakukan verifikasi terhadap pembayaran anda. Mohon tunggu beberapa saat.";
    public static final String HISTORY_PAYMENT_STATUS_WAITING_FOT_PAYMENT_NOTE =
            "Harap segera melakukan pembayaran dalam 1x24 jam. " +
                    "Pembayaran akan otomatis dibatalkan apabila melebihi waktu 1x24 jam. " +
                    "\\n \\n Setelah melakukan transfer, harap tekan tombol konfirmasi\"";


    public static final int HISTORY_CLAIM_STATUS_WAITING_FOR_VERIFICATION = 0;
    public static final int HISTORY_CLAIM_STATUS_ACCEPTED = 1;
    public static final int HISTORY_CLAIM_STATUS_REJECTED = 2;

    public static final String HISTORY_CLAIM_STATUS_WAITING_FOR_VERIFICATION_STRING = "Menunggu Verifikasi";
    public static final String HISTORY_CLAIM_STATUS_ACCEPTED_STRING = "Diterima";
    public static final String HISTORY_CLAIM_STATUS_REJECTED_STRING = "Ditolak";

    public static final String HISTORY_CLAIM_STATUS_WAITING_FOR_VERIFICATION_NOTE =
            "Sistem sedang melakukan verifikasi terhadap klaim anda. Mohon tunggu beberapa saat.";
    public static final String HISTORY_CLAIM_STATUS_ACCEPTED_NOTE =
            "Klaim Anda diterima. Dana akan dikirimkan ke rekening Anda dalam maksimal 12 jam.";
    public static final String HISTORY_CLAIM_STATUS_REJECTED_NOTE =
            "Klaim Anda ditolak karena berkas yang Anda lampirkan tidak valid.";

    public static final String KEY_UNREGISTERED_MEMBERS_JSON = "UNREGISTERED_MEMBERS";

    public static final int DPGK_SERVICE_ID_INTEGER = 1;
    public static final int DKK_SERVICE_ID_INTEGER = 2;
    public static final int DWK_SERVICE_ID_INTEGER = 3;
}
