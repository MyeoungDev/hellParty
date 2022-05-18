const dropdownBtn = document.getElementById("dropdown_btn");



var toolbarOptions = [

    [{ 'font': [] }],
    [{ 'size': ['small', false, 'large', 'huge'] }],  // custom dropdown

    [{ 'align': [] }],
    [{ 'color': [] }, { 'background': [] }],          // dropdown with defaults from theme

    ['bold', 'italic', 'underline', 'strike'],        // toggled buttons
    ['blockquote', 'code-block'],

    [{ 'list': 'ordered'}, { 'list': 'bullet' }],
];

var quill = new Quill('#editor', {
    modules: {
        toolbar: toolbarOptions
    },
    placeholder: '자세한 내용을 입력해주세요!',
    // readOnly: true,  -> 이거는 보여줄 때 사용하면 될 듯
    theme: 'snow'
});
