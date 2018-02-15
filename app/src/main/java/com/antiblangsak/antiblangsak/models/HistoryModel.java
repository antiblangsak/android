package com.antiblangsak.antiblangsak.models;

import android.graphics.Color;

import com.antiblangsak.antiblangsak.R;

/**
 * Created by Syukri on 1/28/18.
 */

public class HistoryModel {
    private int id;
    private String type;
    private String status;
    private String createdAt;
    private int statusColor;

    public HistoryModel(int id, String type, int status, String createdAt) {
        this.type = type;
        this.id = id;
        this.createdAt = createdAt;
        if (type.equals("Pembayaran")) {
            if (status == 0) {
                this.status = "Menunggu Pembayaran";
                this.statusColor = R.color.waiting;
            } else if (status == 1) {
                this.status = "Menunggu Verifikasi";
                this.statusColor = R.color.waiting;
            } else if (status == 2) {
                this.status = "Diterima";
                this.statusColor = R.color.accepted;
            } else {
                this.status = "Ditolak";
                this.statusColor = R.color.rejected;
            }
        } else {
            if (status == 0) {
                this.status = "Menunggu Verifikasi";
                this.statusColor = R.color.waiting;
            } else if (status == 1) {
                this.status = "Diterima";
                this.statusColor = R.color.accepted;
            } else {
                this.status = "Ditolak";
                this.statusColor = R.color.rejected;
            }
        }
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return this.status;
     }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getStatusColor() {
        return statusColor;
    }
}
