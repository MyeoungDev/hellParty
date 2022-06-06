function quilljsediterInit(){
    var option = {
        modules: {
            toolbar: [
                [{header: [1,2,false] }],
                ['bold', 'italic', 'underline'],
                ['image', 'code-block'],
                [{ list: 'ordered' }, { list: 'bullet' }]
            ]
        },
        placeholder: '자세한 내용을 입력해 주세요!',
        theme: 'snow'
    };

    quill = new Quill('#editor', option);
    quill.on('text-change', function() {
        document.getElementById("quill_html").value = quill.root.innerHTML;
    });

    quill.getModule('toolbar').addHandler('image', function () {
        selectLocalImage();
    });
}

/* 이미지 콜백 함수 */

function selectLocalImage() {
    const fileInput = document.createElement('input');
    fileInput.setAttribute('type', 'file');
    console.log("input.type " + fileInput.type);

    fileInput.click();

    fileInput.addEventListener("change", function () {  // change 이벤트로 input 값이 바뀌면 실행
        const formData = new FormData();
        const file = fileInput.files[0];
        console.log(file.name);
        console.log(file.size);
        console.log(file.type);
        formData.append('uploadFile', file);

        $.ajax({
            type: 'post',
            enctype: 'multipart/form-data',
            url: '/board/register/imageUpload',
            data: formData,
            processData: false,
            contentType: false,
            dataType: 'json',
            success: function (data) {
                console.log("ajax success");
                console.log(data)
                const range = quill.getSelection(); // 사용자가 선택한 에디터 범위
                console.log(data.uploadPath);
                data.uploadPath = data.uploadPath.replace(/\\/g, '/');
                console.log("new uploadPath : " + data.uploadPath);
                console.log(data.uuid);
                console.log(data.fileName);
                quill.insertEmbed(range.index, 'image', "/board/display?fileName=" + data.uploadPath +"/"+ data.uuid +"_"+ data.fileName);
                console.log(range);

            },
            error: function (err) {
                console.log("ajax error");
                console.log(err);
            }
        });

    });

    // input.onchange = function () {
    //     const formData = new FormData();
    //     const file = input.files[0];
    //     formData.append('uploadFile', file);
    //     console.log("formData.get('uploadFile')" + JSON.stringify(formData.get('uploadFile')));
    //
    //     $.ajax({
    //         type: 'post',
    //         enctype: 'multipart/form-data',
    //         url: '/board/register/imageUpload',
    //         data: formData,
    //         processData: false,
    //         contentType: false,
    //         // beforeSend: function (xhr){
    //         //     xhr.setRequestHeader($())
    //         // }
    //         success: function (data) {
    //             const range = quill.getSelection();
    //             quill.insertEmbed(range.index, 'image', 'http:/localhost:8080/upload/' + data);
    //         },
    //         error: function (err) {
    //             console.log('Error' + err);
    //         }
    //     });
    // };
}

quilljsediterInit();