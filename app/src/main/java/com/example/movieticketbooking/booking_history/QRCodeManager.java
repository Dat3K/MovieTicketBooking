package com.example.movieticketbooking.booking_history;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.example.movieticketbooking.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeManager {
    public static Bitmap generateQRCode(Context context, String data) {
        int colorBlack = ContextCompat.getColor(context, R.color.colorBlack);
        int colorWhite = ContextCompat.getColor(context, R.color.whiteTextColor);
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            int width = 500;
            int height = 500;
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? colorBlack : colorWhite);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showQRCodeDialog(Context context, Bitmap qrCodeBitmap) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.bh_qr_code_view, null);
        ImageView qrCodeImageView = dialogView.findViewById(R.id.bh_qr_image);
        qrCodeImageView.setImageBitmap(qrCodeBitmap);

        builder.setView(dialogView);



        ImageButton closeButton = dialogView.findViewById(R.id.mp_close_button);
        closeButton.setVisibility(View.GONE);


        builder.setTitle(R.string.qr_code_dialog_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked OK, do nothing or add any additional actions
            }
        });

        builder.show();
    }
}
