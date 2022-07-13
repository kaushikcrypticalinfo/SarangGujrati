package com.saranggujrati.ui.fragment

import android.Manifest
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.saranggujrati.ui.viewModel.EditProfilelViewModel

import android.content.Intent

import android.provider.MediaStore

import android.content.DialogInterface
import android.net.Uri
import com.saranggujrati.R

import android.graphics.Bitmap


import android.app.Activity.RESULT_OK
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.saranggujrati.databinding.FragmentEditProfileBinding
import com.saranggujrati.ui.activity.MainActivity
import com.performly.ext.obtainViewModel
import java.io.*

import android.app.Activity.RESULT_CANCELED
import android.content.ContentResolver
import android.content.Context

import android.graphics.Bitmap.CompressFormat
import android.util.Patterns
import android.view.Menu
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.saranggujrati.AppClass
import com.saranggujrati.ui.*
import com.saranggujrati.ui.activity.LoginActivity
import com.saranggujrati.webservice.Resource
import com.facebook.FacebookSdk.getCacheDir
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import net.simplifiedcoding.imageuploader.UploadRequestBody

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody


class FragmentEditProfile : BaseFragment<EditProfilelViewModel>(), View.OnClickListener {

    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var contentResolver: ContentResolver
    private var selectedImage: Uri? = null

    override fun getLayoutView(inflater: LayoutInflater, container: ViewGroup?): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initializeViewModel(): EditProfilelViewModel {
        return obtainViewModel(EditProfilelViewModel::class.java)
    }

    override fun setUpChildUI(savedInstanceState: Bundle?) {
        mActivity = (activity as MainActivity)
        mActivity.toolbar.title = getString(R.string.edit_profile)
        mActivity.enableViews(true)

        binding.etEditProfileEmail.enable(false)
        binding.etEditProfileEmail.isFocusable = false

        attachListner()
        setHasOptionsMenu(true);
        getProfile()

    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.logo).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    private fun attachListner() {
        binding.ivProfile.setOnClickListener(this)
        binding.cardView.setOnClickListener(this)
        binding.tvUpdateProfile.setOnClickListener(this)
        binding.tvDeleteProfile.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            mActivity.REQUEST_ID_MULTIPLE_PERMISSIONS -> if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    requireContext(),
                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT
                )
                    .show()
            } else if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(
                    requireContext(),
                    "FlagUp Requires Access to Your Storage.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                selectImage()
            }
        }


    }

    //code for select image from gallary or camera
    private fun selectImage() {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_photo),
            getString(R.string.choose_from_gallary),
            getString(R.string.cancel)
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.add_photo))
        builder.setItems(options, DialogInterface.OnClickListener { dialog, item ->

            if (options[item] == getString(R.string.take_photo)) {
                // Open the camera and get the photo
                openCamare()
            } else if (options[item] == getString(R.string.choose_from_gallary)) {
                // choose from  external storage
                openImageChooser()
            } else if (options[item] == getString(R.string.cancel)) {
                dialog.dismiss()
            }
        })
        builder.show()
    }

    private fun openCamare() {
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePicture, 0)
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, 1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode !== RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode === RESULT_OK) {

                    binding.ivProfile.setImageBitmap(data!!.extras!!.get("data") as Bitmap?)
                    getImageUri(requireContext(), (data!!.extras!!.get("data") as Bitmap?)!!)
                    uploadImage()


                }
                1 -> if (resultCode === RESULT_OK) {

                    selectedImage = data?.data!!
                    binding.ivProfile.setImageURI(selectedImage)
                    uploadImage()

                }
            }
        }
    }

    fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.getContentResolver(),
            inImage,
            "Title",
            null
        )
        selectedImage = Uri.parse(path)
        return selectedImage

    }


    private fun uploadImage() {
        contentResolver = requireContext().contentResolver
        val parcelFileDescriptor =
            contentResolver.openFileDescriptor(selectedImage!!, "r", null) ?: return


        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(getCacheDir(), contentResolver.getFileName(selectedImage!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        val body = UploadRequestBody(file, "image")
        viewModel.updateProfilePhoto(
            RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                SavedPrefrence.getUserId(AppClass.appContext).toString()
            ), MultipartBody.Part.createFormData(
                "photo",
                file.getName(),
                body
            )
        )
        /*viewModel.updateProfilePhoto(
            MultipartBody.Part.createFormData("photo", file.name, body))*/

        setupObserversUploadPhoto()
        //Log.e("image", MultipartBody.Part.createFormData("photo", file.name, body).toString())


    }


    private fun showAlertDialogForDeleteAccount() {
        val alertDialog: androidx.appcompat.app.AlertDialog.Builder =
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
        alertDialog.setTitle(R.string.delete_account_title)
        alertDialog.setMessage(R.string.delete_account_message)
        alertDialog.setPositiveButton(R.string.yes) { _, _ ->
            deleteAccount()
        }

        alertDialog.setNegativeButton(R.string.no) { _, _ -> }
        val alert: androidx.appcompat.app.AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()

    }

    private fun deleteAccount() {
        viewModel.deleteAccount(SavedPrefrence.getUserId(AppClass.appContext).toString())

        setupObserversDeleteAccount()
    }

    private fun getProfile() {
        viewModel.getProfile(SavedPrefrence.getUserId(requireContext()).toString())

        setupObserversGetProfile()
    }

    //Upload Photo
    private fun setupObserversUploadPhoto() {

        viewModel.updateProfilePhotoResponse.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    //binding.progressBar.progressbar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        //binding.progressBar.progressbar.visible(false)
                        lifecycleScope.launch {

                            //Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                            //Log.e("sucess", it.value.message)


                        }

                    }

                }
                is Resource.Failure -> {
                    //binding.progressBar.progressbar.visible(false)

                    if (it != null) {
                        //Log.e("error", it.value.message)

                    }

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(AppClass.appContext)) {
                                Snackbar.make(
                                    binding.layout,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        else -> {
                            Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG)
                                .show()

                        }


                    }

                }


            }
        })

    }

    //Get Profile
    private fun setupObserversGetProfile() {

        viewModel.getProfileResponse.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        binding.progressbar.visible(false)
                        lifecycleScope.launch {

                            /* Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG).show()
                             //Log.e("sucess",it.value.message)*/

                            binding.etEditProfileEmail.setText(it.value.data.email)
                            //binding.etEditProfilePassword.setText(it.value.data.otp)
                            binding.etEditProfileName.setText(it.value.data.name)
                            binding.etEditProfilePhoneNumber.setText(it.value.data.phone)

                            Glide.with(AppClass.appContext)
                                .load(it.value.data.photo)
                                .apply(
                                    RequestOptions.placeholderOf(R.drawable.placeholder)
                                        .error(R.drawable.placeholder)
                                )
                                .into(binding.ivProfile);

                        }

                    }

                }
                is Resource.Failure -> {
                    binding.progressbar.visible(false)
                    //Log.e("error", it.value.message)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(AppClass.appContext)) {
                                Snackbar.make(
                                    binding.layout,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        else -> {
                            Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG)
                                .show()

                        }


                    }

                }


            }
        })

    }


    //Delete Account

    private fun setupObserversDeleteAccount() {

        viewModel.deleteAccountResponse.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        binding.progressbar.visible(false)
                        lifecycleScope.launch {
                            Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG)
                                .show()
                            SavedPrefrence.clearPrefrence(AppClass.appContext)
                            val i = Intent(requireContext(), LoginActivity::class.java)
                            startActivity(i)
                        }

                    }

                }
                is Resource.Failure -> {
                    binding.progressbar.visible(false)
                    //Log.e("error", it.value.message)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(AppClass.appContext)) {
                                Snackbar.make(
                                    binding.layout,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        else -> {
                            Snackbar.make(binding.layout, it.value.message, Snackbar.LENGTH_LONG)
                                .show()

                        }


                    }

                }


            }
        })

    }


    //Edit Profile
    private fun setupObserversUpdateProfile() {

        viewModel.editProfileResponse.observe(this, Observer {

            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visible(true)
                }
                is Resource.Success -> {
                    if (it.value.status) {
                        binding.progressbar.visible(false)
                        lifecycleScope.launch {
                            Toast.makeText(requireContext(), it.value.message, Toast.LENGTH_SHORT)
                                .show();
                            //Snackbar.make(binding.tvUpdateProfile, it.value.message, Snackbar.LENGTH_LONG).show()
                            //Log.e("sucess", it.value.message)

                            SavedPrefrence.setEmail(AppClass.appContext, it.value.data.email)
                            SavedPrefrence.setPhone(AppClass.appContext, it.value.data.phone)
                            SavedPrefrence.setUserName(AppClass.appContext, it.value.data.name)

                            getProfile()
                            //SavedPrefrence.setEmail(AppClass.appContext,it.value.data.email)

                            mActivity.onBackPressed()


                        }

                    }

                }
                is Resource.Failure -> {
                    binding.progressbar.visible(false)
                    //Log.e("error", it.value.message)

                    when {
                        it.isNetworkError -> {
                            if (!isOnline(AppClass.appContext)) {
                                Snackbar.make(
                                    binding.tvUpdateProfile,
                                    resources.getString(R.string.check_internet),
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                        else -> {
                            Snackbar.make(
                                binding.tvUpdateProfile,
                                it.value.message,
                                Snackbar.LENGTH_LONG
                            ).show()

                        }


                    }

                }


            }
        })

    }


    private fun validate() {
        if (binding.etEditProfileName.text.toString().isEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.v_name),
                Toast.LENGTH_SHORT
            )
                .show();

        } else if (binding.etEditProfileName.text.toString().length < 4) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.v_valid_name),
                Toast.LENGTH_SHORT
            )
                .show();

        } else if (binding.etEditProfilePhoneNumber.text.toString()
                .isNotEmpty() && binding.etEditProfilePhoneNumber.text.toString().length < 10 || binding.etEditProfilePhoneNumber.text.toString().length > 10
        ) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.v_valid_phone),
                Toast.LENGTH_SHORT
            ).show();

        } else if (binding.etEditProfileEmail.text.toString().isEmpty()) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.v_email),
                Toast.LENGTH_SHORT
            )
                .show();
        } else if (!binding.etEditProfileEmail.text.toString()
                .isEmpty() && !Patterns.EMAIL_ADDRESS.matcher(binding.etEditProfileEmail.text.toString())
                .matches()
        ) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.v_valid_email),
                Toast.LENGTH_SHORT
            ).show();
        } else if (binding.etEditProfilePassword.text.toString().isNotEmpty() &&
            binding.etEditProfilePassword.text.toString().length < 8
        ) {
            Toast.makeText(
                requireContext(),
                resources.getString(R.string.v_valid_password),
                Toast.LENGTH_SHORT
            ).show();

        } else {

            if (binding.etEditProfilePassword.text.toString().isNotEmpty()) {
                viewModel.editProfileWithPassword(
                    SavedPrefrence.getUserId(AppClass.appContext).toString(),
                    binding.etEditProfileEmail.text.toString(),
                    binding.etEditProfilePassword.text.toString(),
                    binding.etEditProfilePhoneNumber.text.toString(),
                    binding.etEditProfileName.text.toString()

                )
            } else {
                viewModel.editProfileWithoutPassword(
                    SavedPrefrence.getUserId(AppClass.appContext).toString(),
                    binding.etEditProfileEmail.text.toString(),
                    binding.etEditProfilePhoneNumber.text.toString(),
                    binding.etEditProfileName.text.toString()
                )
            }
            setupObserversUpdateProfile()
        }

    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.cardView -> if (mActivity.checkAndRequestPermissions(requireActivity())) {
                selectImage()
            }

            binding.ivProfile -> if (mActivity.checkAndRequestPermissions(requireActivity())) {
                selectImage()
            }

            binding.tvUpdateProfile -> validate()
            binding.tvDeleteProfile -> showAlertDialogForDeleteAccount()

        }
    }
}
