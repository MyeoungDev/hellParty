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
    const input = document.createElement('input');
    input.setAttribute('type', 'file');
    input.click();

    input.onchange = function () {
        const fd = new FormData();
        const file = $(this)[0].files[0];
        fd.append('image', file);

        $.ajax({
            type: 'post',
            enctype: 'mulipart/form-data',
            url: '/board/register/imageUpload',
            data: fd,
            processData: false,
            contentType: false,
            // beforeSend: function (xhr){
            //     xhr.setRequestHeader($())
            // }
            success: function (data) {
                const range = quill.getSelection();
                quill.insertEmbed(range.index, 'image', 'http:/localhost:8080/upload/' + data);
            },
            error: function (err) {
                console.log('Error' + err);
            }
        });
    };
}

quilljsediterInit();