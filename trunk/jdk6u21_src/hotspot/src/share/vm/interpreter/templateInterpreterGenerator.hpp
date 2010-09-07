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

// This file contains the platform-independent parts
// of the template interpreter generator.

#ifndef CC_INTERP

class TemplateInterpreterGenerator: public AbstractInterpreterGenerator {
 protected:

  // entry points for shared code sequence
  address _unimplemented_bytecode;
  address _illegal_bytecode_sequence;

  // shared code sequences
  // Converter for native abi result to tosca result
  address generate_result_handler_for(BasicType type);
  address generate_slow_signature_handler();
  address generate_error_exit(const char* msg);
  address generate_StackOverflowError_handler();
  address generate_exception_handler(const char* name, const char* message) {
    return generate_exception_handler_common(name, message, false);
  }
  address generate_klass_exception_handler(const char* name) {
    return generate_exception_handler_common(name, NULL, true);
  }
  address generate_exception_handler_common(const char* name, const char* message, bool pass_oop);
  address generate_ClassCastException_handler();
  address generate_WrongMethodType_handler();
  address generate_ArrayIndexOutOfBounds_handler(const char* name);
  address generate_continuation_for(TosState state);
  address generate_return_entry_for(TosState state, int step);
  address generate_earlyret_entry_for(TosState state);
  address generate_deopt_entry_for(TosState state, int step);
  address generate_safept_entry_for(TosState state, address runtime_entry);
  void    generate_throw_exception();

  // entry point generator
//   address generate_method_entry(AbstractInterpreter::MethodKind kind);

  // Instruction generation
  void generate_and_dispatch (Template* t, TosState tos_out = ilgl);
  void set_vtos_entry_points (Template* t, address& bep, address& cep, address& sep, address& aep, address& iep, address& lep, address& fep, address& dep, address& vep);
  void set_short_entry_points(Template* t, address& bep, address& cep, address& sep, address& aep, address& iep, address& lep, address& fep, address& dep, address& vep);
  void set_wide_entry_point  (Template* t, address& wep);

  void set_entry_points(Bytecodes::Code code);
  void set_unimplemented(int i);
  void set_entry_points_for_all_bytes();
  void set_safepoints_for_all_bytes();

  // Helpers for generate_and_dispatch
  address generate_trace_code(TosState state)   PRODUCT_RETURN0;
  void count_bytecode()                         PRODUCT_RETURN;
  void histogram_bytecode(Template* t)          PRODUCT_RETURN;
  void histogram_bytecode_pair(Template* t)     PRODUCT_RETURN;
  void trace_bytecode(Template* t)              PRODUCT_RETURN;
  void stop_interpreter_at()                    PRODUCT_RETURN;

  void generate_all();

 public:
  TemplateInterpreterGenerator(StubQueue* _code);

  #include "incls/_templateInterpreterGenerator_pd.hpp.incl"

};

#endif // !CC_INTERP
