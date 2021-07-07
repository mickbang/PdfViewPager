/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.voghdev.pdfviewpager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import es.voghdev.pdfviewpager.library.RemotePDFView;

public class RemotePDFActivity extends BaseSampleActivity {
    EditText etPdfUrl;
    Button btnDownload;
    RemotePDFView mRemotePDFView;
    ProgressBar pb_pdf;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.remote_pdf_example);
        setContentView(R.layout.activity_remote_pdf);

        etPdfUrl = findViewById(R.id.et_pdfUrl);
        btnDownload = findViewById(R.id.btn_download);
        mRemotePDFView = findViewById(R.id.remote_pdf_view);
        pb_pdf = findViewById(R.id.pb_pdf);

        setDownloadButtonListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mRemotePDFView != null) {
            mRemotePDFView.destroy();
        }
    }

    protected void setDownloadButtonListener() {
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRemotePDFView.loadPdf(getUrlFromEditText());
                hideDownloadButton();
            }
        });
    }

    protected String getUrlFromEditText() {
        return etPdfUrl.getText().toString().trim();
    }

    public void showDownloadButton() {
        btnDownload.setVisibility(View.VISIBLE);
    }

    public void hideDownloadButton() {
        btnDownload.setVisibility(View.INVISIBLE);
        pb_pdf.setVisibility(View.VISIBLE);
        mRemotePDFView.setVisibility(View.GONE);
        mRemotePDFView.setLoadListener(new RemotePDFView.OnPdfLoadListener() {
            @Override
            public void onLoadFail(Exception e) {

            }

            @Override
            public void onLoadSuccess(String destPath) {
                pb_pdf.setVisibility(View.GONE);
                mRemotePDFView.setVisibility(View.VISIBLE);
            }
        });
    }
}

