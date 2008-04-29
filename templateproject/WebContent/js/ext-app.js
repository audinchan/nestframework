Ext.app.PagingToolbar = Ext.extend(Ext.PagingToolbar, {
	pageNo: 1,
	firstPageNo: 1,
	lastPageNo: 1,
	prevPageNo: 1,
	nextPageNo: 1,
	paramNames : {pageNo: 'pageNo', pageSize: 'pageSize'},
	displayMsg : '显示 {2} 的 {0} - {1}',
	emptyMsg : '没有数据',
	beforePageText : "第",
	afterPageText : "页 共{0}页",
	firstText : "首页",
	prevText : "上页",
	nextText : "下页",
	lastText : "末页",
	refreshText : "刷新",
	
    // private
    onLoad : function(store, r, o){
        if(!this.rendered){
            this.dsLoaded = [store, r, o];
            return;
        }
      
       this.pageNo = parseInt(store.reader.jsonData.page.currPageNumber);
       this.firstPageNo = parseInt(store.reader.jsonData.page.firstPageNumber);
       this.lastPageNo = parseInt(store.reader.jsonData.page.lastPageNumber);
       this.nextPageNo = parseInt(store.reader.jsonData.page.nextPageNumber);
       this.prevPageNo = parseInt(store.reader.jsonData.page.previousPageNumber);
       
       this.cursor = (this.pageNo - 1) * this.pageSize;

       this.afterTextEl.el.innerHTML = String.format(this.afterPageText, this.lastPageNo);
       this.field.dom.value = this.pageNo;
       this.first.setDisabled(this.pageNo == 1);
       this.prev.setDisabled(this.pageNo == 1);
       this.next.setDisabled(this.pageNo == this.lastPageNo);
       this.last.setDisabled(this.pageNo == this.lastPageNo);
       this.loading.enable();
       this.updateInfo();
    },
    
    // private
    onPagingKeydown : function(e){
        var k = e.getKey(), pageNum;
        if (k == e.RETURN) {
            e.stopEvent();
            if(pageNum = this.readPage()){
                pageNum = Math.min(Math.max(1, pageNum), this.lastPageNo);
                this.doLoad(pageNum);
            }
        }else if (k == e.HOME || k == e.END){
            e.stopEvent();
            pageNum = k == e.HOME ? 1 : this.lastPageNo;
            this.field.dom.value = pageNum;
        }else if (k == e.UP || k == e.PAGEUP || k == e.DOWN || k == e.PAGEDOWN){
            e.stopEvent();
            if(pageNum = this.readPage(d)){
                var increment = e.shiftKey ? 10 : 1;
                if(k == e.DOWN || k == e.PAGEDOWN){
                    increment *= -1;
                }
                pageNum += increment;
                if(pageNum >= 1 & pageNum <= this.lastPageNo){
                    this.field.dom.value = pageNum;
                }
            }
        }
    },
    
    doLoad : function(pageNo){
        var o = {}, pn = this.paramNames;
        o[pn.pageNo] = pageNo;
        o[pn.pageSize] = this.pageSize;
        this.store.load({params:o});
    },
    
    // private
    onClick : function(which){
        var store = this.store;
        switch(which){
            case "first":
                this.doLoad(this.firstPageNo);
            break;
            case "prev":
                this.doLoad(this.prevPageNo);
            break;
            case "next":
                this.doLoad(this.nextPageNo);
            break;
            case "last":
                this.doLoad(this.lastPageNo);
            break;
            case "refresh":
                this.doLoad(this.pageNo);
            break;
        }
    },
    
    readPage : function(){
        var v = this.field.dom.value, pageNum;
        if (!v || isNaN(pageNum = parseInt(v, 10))) {
            this.field.dom.value = this.pageNo;
            return false;
        }
        return pageNum;
    }
    
});