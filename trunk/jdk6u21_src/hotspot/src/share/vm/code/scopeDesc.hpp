/*
 * Copyright (c) 1997, 2009, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

// SimpleScopeDesc is used when all you need to extract from
// a given pc,nmethod pair is a methodOop and a bci. This is
// quite a bit faster than allocating a full ScopeDesc, but
// very limited in abilities.

class SimpleScopeDesc : public StackObj {
 private:
  methodOop _method;
  int _bci;

 public:
  SimpleScopeDesc(nmethod* code,address pc) {
    PcDesc* pc_desc = code->pc_desc_at(pc);
    assert(pc_desc != NULL, "Must be able to find matching PcDesc");
    DebugInfoReadStream buffer(code, pc_desc->scope_decode_offset());
    int ignore_sender = buffer.read_int();
    _method           = methodOop(buffer.read_oop());
    _bci              = buffer.read_bci();
  }

  methodOop method() { return _method; }
  int bci() { return _bci; }
};

// ScopeDescs contain the information that makes source-level debugging of
// nmethods possible; each scopeDesc describes a method activation

class ScopeDesc : public ResourceObj {
 public:
  // Constructor
  ScopeDesc(const nmethod* code, int decode_offset, int obj_decode_offset, bool reexecute, bool return_oop);

  // Calls above, giving default value of "serialized_null" to the
  // "obj_decode_offset" argument.  (We don't use a default argument to
  // avoid a .hpp-.hpp dependency.)
  ScopeDesc(const nmethod* code, int decode_offset, bool reexecute, bool return_oop);

  // JVM state
  methodHandle method()   const { return _method; }
  int          bci()      const { return _bci;    }
  bool should_reexecute() const { return _reexecute; }
  bool return_oop()       const { return _return_oop; }

  GrowableArray<ScopeValue*>*   locals();
  GrowableArray<ScopeValue*>*   expressions();
  GrowableArray<MonitorValue*>* monitors();
  GrowableArray<ScopeValue*>*   objects();

  // Stack walking, returns NULL if this is the outer most scope.
  ScopeDesc* sender() const;

  // Returns where the scope was decoded
  int decode_offset() const { return _decode_offset; }

  // Tells whether sender() returns NULL
  bool is_top() const;
  // Tells whether sd is equal to this
  bool is_equal(ScopeDesc* sd) const;

 private:
  // Alternative constructor
  ScopeDesc(const ScopeDesc* parent);

  // JVM state
  methodHandle  _method;
  int           _bci;
  bool          _reexecute;
  bool          _return_oop;

  // Decoding offsets
  int _decode_offset;
  int _sender_decode_offset;
  int _locals_decode_offset;
  int _expressions_decode_offset;
  int _monitors_decode_offset;

  // Object pool
  GrowableArray<ScopeValue*>* _objects;

  // Nmethod information
  const nmethod* _code;

  // Decoding operations
  void decode_body();
  GrowableArray<ScopeValue*>* decode_scope_values(int decode_offset);
  GrowableArray<MonitorValue*>* decode_monitor_values(int decode_offset);
  GrowableArray<ScopeValue*>* decode_object_values(int decode_offset);

  DebugInfoReadStream* stream_at(int decode_offset) const;


 public:
  // Verification
  void verify();

#ifndef PRODUCT
 public:
  // Printing support
  void print_on(outputStream* st) const;
  void print_on(outputStream* st, PcDesc* pd) const;
  void print_value_on(outputStream* st) const;
#endif
};
