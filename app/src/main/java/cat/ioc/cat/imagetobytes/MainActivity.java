package cat.ioc.cat.imagetobytes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Base64;

public class MainActivity extends AppCompatActivity {
    public static final int PICK_IMAGE = 1;
    TextView textBytes;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button checkImage = findViewById(R.id.checkImage);
        textBytes = findViewById(R.id.textBytes);
        imageView = (ImageView) findViewById(R.id.imageToShow);

        checkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent implicit per a seleccionar la imatge.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        InputStream stream = null;
        if (requestCode == PICK_IMAGE) {
            try {
                //Obtenim la imatge seleccionada per l'usuari.
                Uri selectedImageUri = data.getData();

                //Obtenim els bytes de la imatge.
                stream =   getContentResolver().openInputStream(selectedImageUri);
                byte[] imatgeEnBytes = getBytes(stream);
                textBytes.setText(Arrays.toString(imatgeEnBytes));
                Log.i("byte[]",Arrays.toString(imatgeEnBytes));
                //Obtenim la imatge a partir dels bytes i la mostrem en l'imageView.
                Bitmap bmp = BitmapFactory.decodeByteArray(imatgeEnBytes, 0, imatgeEnBytes.length);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getMaxWidth(), imageView.getMaxHeight(), false));
            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                if(stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);

        }

        Log.i("byte[]", String.valueOf(byteBuffer));
        return byteBuffer.toByteArray();
    }
}